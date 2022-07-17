package com.patrykcelinski.traffic.domain.model
import cats.implicits._

case class Intersection(avenue: Avenue, street: Street) {
  override def toString: String = s"$avenue$street"
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
