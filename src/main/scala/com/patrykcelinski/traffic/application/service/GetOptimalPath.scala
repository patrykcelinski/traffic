package com.patrykcelinski.traffic.application.service
import cats.Monad
import com.patrykcelinski.traffic.application.error.{
  InvalidInputError,
  TrafficAppError
}
import com.patrykcelinski.traffic.application.input.FindOptimalPathQuery
import com.patrykcelinski.traffic.domain.model.{Measurements, OptimalPath}
import cats.implicits._
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

    def mapPathfindingErrorToTrafficAppError(
        error: PathfindingError
    ): Either[TrafficAppError, OptimalPath] =
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

    def getPath(
        measurements: Measurements
    ): Either[TrafficAppError, OptimalPath] =
      OptimalPath
        .calculate(
          query.startingIntersection,
          query.endingIntersection,
          measurements.getRoutesTransitTimes()
        ) match {
        case Left(error)  =>
          mapPathfindingErrorToTrafficAppError(error)
        case Right(value) => value.asRight
      }

    for {
      maybeMeasurements <-
        readMeasurementsRepository.getMeasurements(query.measurementsFilePath)
      output             = maybeMeasurements match {
                             case Left(error: InvalidInputError) =>
                               error.asLeft
                             case Right(measurements)            =>
                               getPath(measurements)
                           }
    } yield output
  }

}
