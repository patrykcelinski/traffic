package com.patrykcelinski.traffic.infrastructure.filesystem

import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.domain.model.Measurements
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository
import io.circe.parser.decode

import java.io.{File, FileInputStream}
import java.nio.file.Path
import scala.io.Source

class FileBasedReadMeasurementsRepository[F[_]: Sync]
    extends ReadMeasurementsRepository[F] {

  import com.patrykcelinski.traffic.application.serialization.MeasurementsCodecs._

  override def getMeasurements(
      measurementsFilePath: Path
  ): F[Option[Measurements]] =
    Resource
      .fromAutoCloseable(
        Sync[F].delay(
          Source.fromFile(new File(measurementsFilePath.toString))
        )
      )
      .use { source =>
        decode[Measurements](source.mkString) match {
          case Left(error)         => throw error
          case Right(measurements) => measurements.some.pure[F]
        }
      }
}
