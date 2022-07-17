package com.patrykcelinski.traffic.domain.model.graph

import cats.implicits._
import com.patrykcelinski.traffic.domain.model.graph.cost.{
  HeuristicFunctionCost,
  PathCost,
  TotalCost
}

import scala.annotation.tailrec

// DirectedEdgeWeightedGraph
class Graph[T](graph: Map[T, Set[Edge[T]]]) {

  // a number of edges connected to a node
  def inDegree(node: T): Int =
    graph.values.count(_.exists(_.to == node))

  def findPath(
      start: T,
      end: T,
      heuristicFunction: (T, T) => HeuristicFunctionCost = (_, _) =>
        HeuristicFunctionCost(0.0)
  ): Either[PathfindingError, Path[T]] = {

    case class Node(
        key: T,
        totalCostSoFar: TotalCost,
        previous: Option[T]
    )

    def retracePath(endingNode: Node, closed: Set[Node]): Path[T] = {

      @tailrec
      def retracePathTailrec(node: Node, path: List[T]): List[T] =
        node.previous match {
          case None           => (path :+ start).reverse
          case Some(previous) =>
            val previousNode = closed.find(_.key == previous).get
            retracePathTailrec(previousNode, path :+ node.key)
        }

      val path = retracePathTailrec(endingNode, Nil)
      Path(endingNode.totalCostSoFar, path)
    }

    @tailrec
    def findPathTailrec(open: List[Node], closedSet: Set[Node]): Path[T] =
      open match {
        case currentNode :: _ if currentNode.key == end =>
          retracePath(currentNode, closedSet)
        case currentNode :: restOfOpenNodes             =>
          val neighbours =
            graph(currentNode.key)
              .filter(edge => !closedSet.exists(n => n.key == edge.to))
              .map(edge =>
                Node(
                  edge.to,
                  currentNode.totalCostSoFar.add(edge.cost),
                  edge.from.some
                )
              )
              .toList
          val newOpen    =
            (neighbours ++ restOfOpenNodes)
              .sortWith { case (x1, x2) =>
                val costEdge1 =
                  x1.totalCostSoFar + heuristicFunction(x1.key, end)
                val costEdge2 =
                  x2.totalCostSoFar + heuristicFunction(x2.key, end)
                costEdge1 < costEdge2
              }

          val newClosed = closedSet + currentNode

          findPathTailrec(
            newOpen,
            newClosed
          )
      }

    if (!graph.contains(start))
      PathfindingError.NotExistingStartingNode.asLeft
    else if (!graph.contains(end))
      PathfindingError.NotExistingEndingNode.asLeft
    else if (this.inDegree(end) >= 1)
      findPathTailrec(
        List(Node(start, TotalCost.ZERO, None)),
        Set.empty
      ).asRight
    else
      PathfindingError.NoPath.asLeft
  }
}
