package com.patrykcelinski.traffic.domain.model

case class OptimalPath(
    startingIntersection: Intersection,
    endingIntersection: Intersection,
    path: List[Intersection],
    totalTransitTime: Double
)

object OptimalPath {
  def calculate(
      startingIntersection: Intersection,
      endingIntersection: Intersection,
      routeTransitTimes: List[RouteTransitTime]
  ): OptimalPath = {
    val averageIntersectionTransitTimes =
      routeTransitTimes
        .groupBy(_.route)
        .map { case (route, transitionTimes) =>
          AverageIntersectionTransitTime.calculate(
            route,
            transitionTimes
          )
        }

    // TODO :convert to graph and find path

    OptimalPath(
      startingIntersection,
      endingIntersection,
      path = Nil,
      totalTransitTime = 0.0
    )
  }
}
