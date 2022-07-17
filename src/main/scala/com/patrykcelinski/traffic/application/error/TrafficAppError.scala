package com.patrykcelinski.traffic.application.error

import com.patrykcelinski.traffic.domain.model.IntersectionKey

import java.nio.file.Path

sealed trait TrafficAppError extends Product with Serializable

object TrafficAppError {
  case class IntersectionDoesNotExit(intersectionKey: IntersectionKey)
      extends TrafficAppError
  case class ThereIsNoPathBetween(start: IntersectionKey, end: IntersectionKey)
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
}
