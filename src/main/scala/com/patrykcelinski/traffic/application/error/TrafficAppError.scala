package com.patrykcelinski.traffic.application.error

import com.patrykcelinski.traffic.domain.model.Intersection

import java.nio.file.Path

sealed trait TrafficAppError extends Product with Serializable

object TrafficAppError {
  case class IntersectionDoesNotExit(intersection: Intersection)
      extends TrafficAppError
  case class ThereIsNoPathBetween(start: Intersection, end: Intersection)
      extends TrafficAppError
}

sealed trait InvalidInputError extends TrafficAppError

object InvalidInputError {
  case class InvalidMeasurementsDataFilePath(
      invalidMeasurementsDataFilePath: String
  ) extends InvalidInputError
  case class InvalidIntersection(invalidIntersection: String)
      extends InvalidInputError
  case class NotExistingMeasurementsDataFilePath(
      notExistingMeasurementsDataFilePath: Path
  ) extends InvalidInputError
  case object FileIsInWrongFormat extends InvalidInputError
}
