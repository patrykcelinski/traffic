package com.patrykcelinski.traffic.domain.model

import com.patrykcelinski.traffic.domain.model.graph.cost.{PathCost, TotalCost}
import com.patrykcelinski.traffic.domain.model.graph.{
  Edge,
  Graph,
  PathfindingError
}

case class OptimalPath(
    startingIntersection: IntersectionKey,
    endingIntersection: IntersectionKey,
    path: List[IntersectionKey],
    totalTransitTime: TotalCost
)

object OptimalPath {
  def calculate(
      startingIntersection: IntersectionKey,
      endingIntersection: IntersectionKey,
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

    val graphMap: Map[String, Set[Edge[String]]] =
      averageIntersectionTransitTimes
        .map(averageTransitTime =>
          Edge(
            from = averageTransitTime.start.value,
            to = averageTransitTime.end.value,
            cost = PathCost(averageTransitTime.transitTime)
          )
        )
        .groupBy(_.from)
        .map { case (key, edges) =>
          (key, edges.toSet)
        }

    new Graph(graphMap)
      .findPath(startingIntersection.value, endingIntersection.value)
      .map { path =>
        OptimalPath(
          startingIntersection,
          endingIntersection,
          path = path.path.flatMap(IntersectionKey.fromString),
          totalTransitTime = path.totalCost
        )
      }

  }
}
