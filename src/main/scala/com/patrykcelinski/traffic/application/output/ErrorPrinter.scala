package com.patrykcelinski.traffic.application.output

import cats.effect.std.Console
import com.patrykcelinski.traffic.application.error.{
  InvalidInputError,
  TrafficAppError
}

object ErrorPrinter {
  def print[F[_]: Console](error: TrafficAppError): F[Unit] =
    error match {
      case error: InvalidInputError.InvalidIntersection             =>
        Console[F]
          .errorln(
            s"ERROR. Invalid intersection: ${error.invalidIntersection}, should be in format \'{Avenue Char}{Street Integer}\' (regex: ^[A-Z]{1}\\d*$$) "
          )
      case error: InvalidInputError.InvalidMeasurementsDataFilePath =>
        Console[F]
          .errorln(
            s"ERROR. Invalid measurements file path: ${error.invalidMeasurementsDataFilePath}"
          )
      case InvalidInputError.NotExistingMeasurementsDataFilePath(
            notExistingMeasurementsDataFilePath
          ) =>
        Console[F]
          .errorln(
            s"ERROR. Measurements data file does not exit: ${notExistingMeasurementsDataFilePath.toString}"
          )
      case TrafficAppError.ThereIsNoPathBetween(start, end)         =>
        Console[F]
          .errorln(
            s"ERROR. There is no path connecting $start and $end"
          )
      case TrafficAppError.IntersectionDoesNotExit(key)             =>
        Console[F]
          .errorln(
            s"ERROR. Given intersection ${key.toString} does not exist"
          )
      case InvalidInputError.FileIsInWrongFormat                    =>
        Console[F]
          .errorln(
            s"ERROR. Data file is in the wrong format."
          )
    }
}
