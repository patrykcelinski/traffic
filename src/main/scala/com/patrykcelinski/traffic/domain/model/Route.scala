package com.patrykcelinski.traffic.domain.model

case class Route(
    start: Intersection,
    end: Intersection
) {
  override def toString: String =
    s"$start>$end"
}
