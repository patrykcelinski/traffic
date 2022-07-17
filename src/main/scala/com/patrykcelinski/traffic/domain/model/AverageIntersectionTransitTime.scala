package com.patrykcelinski.traffic.domain.model

case class AverageIntersectionTransitTime private (
    start: Intersection,
    transitTime: Seconds,
    end: Intersection
) {
  override def toString: String =
    s"$start>$end:$transitTime"
}

object AverageIntersectionTransitTime {
  def calculate(
      route: Route,
      transitTimes: List[RouteTransitTime]
  ): AverageIntersectionTransitTime =
    AverageIntersectionTransitTime(
      route.start,
      Seconds(transitTimes.map(_.transitTime.value).sum / transitTimes.length),
      route.end
    )
}
