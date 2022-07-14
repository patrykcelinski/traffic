package com.patrykcelinski.traffic.application.error

import java.nio.file.Path

sealed trait InvalidInputError extends Product with Serializable

object InvalidInputError {
  case class InvalidMeasurementsDataFilePath(invalidMeasurementsDataFilePath: String)
      extends InvalidInputError
  case class InvalidIntersection(invalidIntersection: String)
      extends InvalidInputError
  case class NotExistingMeasurementsDataFilePath(notExistingMeasurementsDataFilePath: Path)
    extends InvalidInputError
}
