package com.patrykcelinski.traffic.domain.model

case class RouteTransitTime(
    route: Route,
    transitTime: Seconds
) {
  override def toString: String =
    s"$route:$transitTime"
}
