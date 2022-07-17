package com.patrykcelinski.traffic.application.service
import cats.Monad
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.application.input.FindOptimalPathQuery
import com.patrykcelinski.traffic.domain.model.{
  AverageIntersectionTransitTime,
  OptimalPath
}
import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.application.output.OptimalPathPrinter
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository

trait GetOptimalPath[F[_]] {
  def apply(
      query: FindOptimalPathQuery
  ): F[Either[InvalidInputError, OptimalPath]]
}

object GetOptimalPath {
  def instance[F[_]: Monad](implicit
      readMeasurementsRepository: ReadMeasurementsRepository[F]
  ): GetOptimalPath[F] = (
    query: FindOptimalPathQuery
  ) =>
    for {
      maybeMeasurements <-
        readMeasurementsRepository.getMeasurements(query.measurementsFilePath)
      output             = maybeMeasurements match {
                             case None               =>
                               InvalidInputError
                                 .NotExistingMeasurementsDataFilePath(
                                   query.measurementsFilePath
                                 )
                                 .asLeft
                             case Some(measurements) =>
                               OptimalPath
                                 .calculate(
                                   query.startingIntersection,
                                   query.endingIntersection,
                                   measurements.getRoutesTransitTimes()
                                 )
                                 .asRight
                           }
    } yield output

}
