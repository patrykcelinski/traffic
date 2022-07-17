package com.patrykcelinski.traffic.application.service
import cats.Monad
import com.patrykcelinski.traffic.application.error.{
  InvalidInputError,
  TrafficAppError
}
import com.patrykcelinski.traffic.application.input.FindOptimalPathQuery
import com.patrykcelinski.traffic.domain.model.{
  AverageIntersectionTransitTime,
  OptimalPath
}
import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.application.error.InvalidInputError.NotExistingMeasurementsDataFilePath
import com.patrykcelinski.traffic.application.output.OptimalPathPrinter
import com.patrykcelinski.traffic.domain.model.graph.PathfindingError
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository

trait GetOptimalPath[F[_]] {
  def apply(
      query: FindOptimalPathQuery
  ): F[Either[TrafficAppError, OptimalPath]]
}

object GetOptimalPath {
  def instance[F[_]: Monad](implicit
      readMeasurementsRepository: ReadMeasurementsRepository[F]
  ): GetOptimalPath[F] = (
    query: FindOptimalPathQuery
  ) => {

    for {
      maybeMeasurements <-
        readMeasurementsRepository.getMeasurements(query.measurementsFilePath)
      output             = maybeMeasurements match {
                             case Left(
                                   error: InvalidInputError.NotExistingMeasurementsDataFilePath
                                 ) =>
                               error.asLeft
                             case Left(error: InvalidInputError.FileIsInWrongFormat.type) =>
                               error.asLeft
                             case Right(measurements)                                     =>
                               OptimalPath
                                 .calculate(
                                   query.startingIntersection,
                                   query.endingIntersection,
                                   measurements.getRoutesTransitTimes()
                                 ) match {
                                 case Left(error)  =>
                                   error match {
                                     case PathfindingError.NoPath                  =>
                                       TrafficAppError
                                         .ThereIsNoPathBetween(
                                           query.startingIntersection,
                                           query.endingIntersection
                                         )
                                         .asLeft
                                     case PathfindingError.NotExistingStartingNode =>
                                       TrafficAppError
                                         .IntersectionDoesNotExit(
                                           query.startingIntersection
                                         )
                                         .asLeft
                                     case PathfindingError.NotExistingEndingNode   =>
                                       TrafficAppError
                                         .IntersectionDoesNotExit(
                                           query.endingIntersection
                                         )
                                         .asLeft
                                   }
                                 case Right(value) => value.asRight
                               }
                           }
    } yield output
  }

}
