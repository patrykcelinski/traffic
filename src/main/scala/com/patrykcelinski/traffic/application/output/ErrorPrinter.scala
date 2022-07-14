package com.patrykcelinski.traffic.application.output

import cats.effect.{ExitCode, IO}
import cats.effect.std.Console
import com.patrykcelinski.traffic.application.error.InvalidInputError

object ErrorPrinter {
  def print[F[_]: Console](error: InvalidInputError): F[Unit] =
    error match {
      case error: InvalidInputError.InvalidIntersection             =>
        Console[F]
          .error(
            s"Invalid intersection: ${error.invalidIntersection}, should be in format \'{Avenue Char}{Street Integer}\' (regex: ^[A-Z]{1}\\d*$$) "
          )
      case error: InvalidInputError.InvalidMeasurementsDataFilePath =>
        Console[F]
          .error(
            s"Invalid measurements file path: ${error.invalidMeasurementsDataFilePath}"
          )
      case InvalidInputError.NotExistingMeasurementsDataFilePath(
            notExistingMeasurementsDataFilePath
          ) =>
        Console[F]
          .error(
            s"Measurements data file does not exit: ${notExistingMeasurementsDataFilePath.toString}"
          )
    }
}
