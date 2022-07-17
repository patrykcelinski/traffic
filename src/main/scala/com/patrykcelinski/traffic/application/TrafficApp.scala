package com.patrykcelinski.traffic.application

import cats.implicits._
import cats.effect._
import cats.data._
import com.patrykcelinski.traffic.application.input.FindOptimalPathQuery
import com.patrykcelinski.traffic.domain.model.{
  Avenue,
  AverageIntersectionTransitTime,
  IntersectionKey,
  OptimalPath,
  Route,
  RouteTransitTime,
  Street,
  TransitMeasurement
}
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository
import cats.effect.std.Console
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.application.output.{
  ErrorPrinter,
  OptimalPathPrinter
}
import cats.effect.std.Console
import cats.syntax.all._
import com.patrykcelinski.traffic.application.service.GetOptimalPath
import com.patrykcelinski.traffic.infrastructure.filesystem.FileBasedReadMeasurementsRepository

object TrafficApp extends IOApp {

  implicit val measurementsRepository: ReadMeasurementsRepository[IO] =
    new FileBasedReadMeasurementsRepository()
  implicit val console: Console[IO]                                   = Console.make[IO]
  implicit val getOptimalPath: GetOptimalPath[IO]                     = GetOptimalPath.instance[IO]

  override def run(args: List[String]): IO[ExitCode] =
    FindOptimalPathQuery
      .fromArgs(args) match {
      case Left(error)  =>
        ErrorPrinter.print(error).as(ExitCode.Error)
      case Right(query) =>
        program(query)
    }

  private def program(query: FindOptimalPathQuery) = {
    getOptimalPath(query).flatMap {
      case Left(error)   =>
        ErrorPrinter.print(error).as(ExitCode.Error)
      case Right(result) =>
        OptimalPathPrinter.print(result).as(ExitCode.Success)
    }
  }
}
