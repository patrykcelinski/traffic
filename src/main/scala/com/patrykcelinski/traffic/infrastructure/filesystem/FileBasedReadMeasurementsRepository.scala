package com.patrykcelinski.traffic.infrastructure.filesystem

import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.domain.model.Measurements
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository
import io.circe.parser.decode

import java.io.{File, FileInputStream, FileNotFoundException}
import java.nio.file.Path
import scala.io.Source

class FileBasedReadMeasurementsRepository[F[_]: Sync]
    extends ReadMeasurementsRepository[F] {

  import com.patrykcelinski.traffic.application.serialization.MeasurementsCodecs._

  override def getMeasurements(
      measurementsFilePath: Path
  ): F[Either[InvalidInputError, Measurements]] =
    Resource
      .fromAutoCloseable(
        Sync[F].delay(
          Source.fromFile(new File(measurementsFilePath.toString))
        )
      )
      .attempt
      .use {
        case Left(_: FileNotFoundException) =>
          InvalidInputError
            .NotExistingMeasurementsDataFilePath(measurementsFilePath)
            .asLeft[Measurements]
            .pure[F]
            .widen
        case Right(source)                  =>
          decode[Measurements](source.mkString) match {
            case Left(_)             =>
              InvalidInputError.FileIsInWrongFormat.asLeft.pure[F].widen
            case Right(measurements) => measurements.asRight.pure[F].widen
          }
      }
}
