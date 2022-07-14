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
      measurements: Measurements
  ): OptimalPath = {
    val routeTransitTimes               =
      measurements.getRoutesTransitTimes()
    val averageIntersectionTransitTimes =
      routeTransitTimes
        .groupBy(_.route)
        .map { case (route, transitionTimes) =>
          AverageIntersectionTransitTime.calculate(
            route,
            transitionTimes
          )
        }

    // TODO: For time being it is dummy data
    OptimalPath(
      startingIntersection,
      endingIntersection,
      path = Nil,
      totalTransitTime = 0.0
    )
  }
}
