package com.patrykcelinski.traffic.domain.model
import cats.implicits._

case class Intersection(avenue: Avenue, street: Street) {
  override def toString: String = s"$avenue$street"

  def distanceTo(other: Intersection): Double = {
    val x1 = avenue.id.toInt - 'A'.toInt
    val y1 = street.id
    val x2 = other.avenue.id.toInt - 'A'.toInt
    val y2 = other.street.id
    math.sqrt(math.pow(x1 - x2, 2) + math.pow(y1 - y2, 2))
  }

}

object Intersection {

  def fromString(intersectionString: String): Option[Intersection] = {
    for {
      maybeAvenueChar          <- intersectionString.headOption
      maybeStreetIntegerString <- intersectionString.drop(1).toIntOption
      maybeIntersection        <-
        if (maybeAvenueChar.isLetter)
          Intersection(
            Avenue(maybeAvenueChar),
            Street(maybeStreetIntegerString)
          ).some
        else none[Intersection]
    } yield maybeIntersection
  }
}
