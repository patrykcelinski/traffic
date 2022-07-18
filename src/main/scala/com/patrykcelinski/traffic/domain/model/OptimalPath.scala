package com.patrykcelinski.traffic.domain.model

import com.patrykcelinski.traffic.domain.model.graph.cost.{
  HeuristicFunctionCost,
  PathCost,
  TotalCost
}
import com.patrykcelinski.traffic.domain.model.graph.{
  Edge,
  Graph,
  PathfindingError
}

case class OptimalPath(
    startingIntersection: Intersection,
    endingIntersection: Intersection,
    path: List[Intersection],
    totalTransitTime: TotalCost
)

object OptimalPath {
  def calculate(
      startingIntersection: Intersection,
      endingIntersection: Intersection,
      routeTransitTimes: List[RouteTransitTime]
  ): Either[PathfindingError, OptimalPath] = {

    val averageIntersectionTransitTimes =
      routeTransitTimes
        .groupBy(_.route)
        .map { case (route, transitionTimes) =>
          AverageIntersectionTransitTime.calculate(
            route,
            transitionTimes
          )
        }

    val graphMap: Map[Intersection, Set[Edge[Intersection]]] =
      averageIntersectionTransitTimes
        .map(averageTransitTime =>
          Edge(
            from = averageTransitTime.start,
            to = averageTransitTime.end,
            cost = PathCost(averageTransitTime.transitTime.value)
          )
        )
        .groupBy(_.from)
        .map { case (key, edges) =>
          (key, edges.toSet)
        }

    val heuristicFunction
        : (Intersection, Intersection) => HeuristicFunctionCost =
      (from, to) => HeuristicFunctionCost(from.distanceTo(to))

    new Graph(graphMap)
      .findPath(startingIntersection, endingIntersection, heuristicFunction)
      .map { optimalPath =>
        OptimalPath(
          startingIntersection,
          endingIntersection,
          path = optimalPath.path,
          totalTransitTime = optimalPath.totalCost
        )
      }
  }
}
