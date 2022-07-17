package com.patrykcelinski.traffic.domain.model
import cats.implicits._

case class IntersectionKey private (value: String) extends AnyVal {
  override def toString: String = value
}

object IntersectionKey {

  def make(avenue: Avenue, street: Street): IntersectionKey =
    IntersectionKey(s"$avenue$street")

  def fromString(intersectionString: String): Option[IntersectionKey] = {
    for {
      maybeAvenueChar          <- intersectionString.headOption
      maybeStreetIntegerString <- intersectionString.drop(1).toIntOption
      maybeIntersection        <-
        if (maybeAvenueChar.isLetter)
          IntersectionKey
            .make(
              Avenue(maybeAvenueChar),
              Street(maybeStreetIntegerString)
            )
            .some
        else none[IntersectionKey]
    } yield maybeIntersection
  }
}
