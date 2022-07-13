package com.patrykcelinski.traffic.domain.model

case class RouteTransitTime(
    route: Route,
    transitTime: Double
) {
  override def toString: String =
    s"$route:$transitTime"
}
