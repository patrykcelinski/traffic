package com.patrykcelinski.traffic.application

import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository
import com.patrykcelinski.traffic.infrastructure.filesystem.FileBasedReadMeasurementsRepository

object TrafficApp extends IOApp {

  val measurementsRepository: ReadMeasurementsRepository[IO] =
    new FileBasedReadMeasurementsRepository("minimized-sample-data.json")

  override def run(args: List[String]): IO[ExitCode] =
    for {
      measurements <- measurementsRepository.getMeasurements()
      _            <- Sync[IO].delay(println(measurements))
    } yield ExitCode.Success
}
