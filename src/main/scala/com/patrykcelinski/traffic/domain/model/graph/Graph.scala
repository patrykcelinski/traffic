package com.patrykcelinski.traffic.domain.model.graph

import cats.implicits._
import com.patrykcelinski.traffic.domain.model.graph.cost.{
  HeuristicFunctionCost,
  TotalCost
}

import scala.annotation.tailrec
import scala.collection.mutable

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
    def findPathTailrec(
        open: mutable.PriorityQueue[Node],
        closedSet: Set[Node]
    ): Path[T] = {
      val currentNode = open.dequeue()
      if (currentNode.key == end)
        retracePath(currentNode, closedSet)
      else {
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
        val newOpen    = open.addAll(neighbours)

        val newClosed = closedSet + currentNode

        findPathTailrec(
          newOpen,
          newClosed
        )
      }
    }

    case object NodeOrdering extends Ordering[Node] {
      override def compare(x: Node, y: Node): Int = {
        val costEdge1 =
          x.totalCostSoFar + heuristicFunction(x.key, end)
        val costEdge2 =
          y.totalCostSoFar + heuristicFunction(y.key, end)
        if (costEdge1 < costEdge2) 1 else -1
      }
    }

    implicit val ordering: Ordering[Node] = NodeOrdering

    if (!graph.contains(start))
      PathfindingError.NotExistingStartingNode.asLeft
    else if (!graph.contains(end))
      PathfindingError.NotExistingEndingNode.asLeft
    else if (this.inDegree(end) >= 1)
      findPathTailrec(
        mutable.PriorityQueue.from(List(Node(start, TotalCost.ZERO, None))),
        Set.empty
      ).asRight
    else
      PathfindingError.NoPath.asLeft
  }
}
