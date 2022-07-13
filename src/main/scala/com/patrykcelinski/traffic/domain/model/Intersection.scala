package com.patrykcelinski.traffic.domain.model

case class Intersection(
    avenue: Avenue,
    street: Street
) {
  override def toString: String =
    s"${avenue.id}${street.id}"
}
