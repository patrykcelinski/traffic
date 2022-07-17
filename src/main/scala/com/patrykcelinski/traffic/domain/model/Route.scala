package com.patrykcelinski.traffic.domain.model

case class Route(
    start: IntersectionKey,
    end: IntersectionKey
) {
  override def toString: String =
    s"$start>$end"
}
