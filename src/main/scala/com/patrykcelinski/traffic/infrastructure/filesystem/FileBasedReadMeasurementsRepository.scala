package com.patrykcelinski.traffic.infrastructure.filesystem

import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.domain.model.Measurements
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository
import io.circe.parser.decode
import scala.io.Source

class FileBasedReadMeasurementsRepository(resourceFileName: String)
    extends ReadMeasurementsRepository[IO] {

  import com.patrykcelinski.traffic.application.serialization.MeasurementsCodecs._

  override def getMeasurements(): IO[Measurements] =
    Sync[IO].delay(
      decode[Measurements](
        Source.fromResource(resourceFileName).mkString
      ) match {
        case Left(error)         => throw error
        case Right(measurements) => measurements
      }
    )
}
