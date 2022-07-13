package com.patrykcelinski.traffic.application

import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.domain.model.{
  Avenue,
  AverageIntersectionTransitTime,
  Intersection,
  Route,
  RouteTransitTime,
  Street,
  TransitMeasurement
}
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository
import com.patrykcelinski.traffic.infrastructure.filesystem.FileBasedReadMeasurementsRepository

object TrafficApp extends IOApp {

  val measurementsRepository: ReadMeasurementsRepository[IO] =
    new FileBasedReadMeasurementsRepository("minimized-sample-data.json")

  override def run(args: List[String]): IO[ExitCode] =
    for {
      measurements                   <- measurementsRepository.getMeasurements()
      routeTransitTimes               =
        measurements.getRoutesTransitTimes()
      averageIntersectionTransitTimes =
        routeTransitTimes
          .groupBy(_.route)
          .map { case (route, transitionTimes) =>
            AverageIntersectionTransitTime.calculate(route, transitionTimes)
          }

      _ <- Sync[IO].delay(
             averageIntersectionTransitTimes
               .foreach(println)
           )
    } yield ExitCode.Success
}
