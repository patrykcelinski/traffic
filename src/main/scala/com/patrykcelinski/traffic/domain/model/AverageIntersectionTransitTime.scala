package com.patrykcelinski.traffic.domain.model

case class AverageIntersectionTransitTime private (
    start: IntersectionKey,
    transitTime: Double,
    end: IntersectionKey
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
      transitTimes.map(_.transitTime).sum / transitTimes.length,
      route.end
    )
}
