package com.patrykcelinski.traffic.application.input

import cats.implicits._
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.domain.model.Intersection

import java.io.File
import java.nio.file.{Path, Paths}
import scala.util.Try

case class FindOptimalPathQuery private (
    measurementsFilePath: Path,
    startingIntersection: Intersection,
    endingIntersection: Intersection
)

object FindOptimalPathQuery {

  def fromArgs(
      args: List[String]
  ): Either[InvalidInputError, FindOptimalPathQuery] =
    args match {
      case measurementsFileLocation :: startingIntersectionString :: endingIntersectionString :: Nil =>
        for {
          startingIntersection <- validateIntersection(
                                    startingIntersectionString
                                  )
          endingIntersection   <- validateIntersection(endingIntersectionString)
          measurementsFilePath <- validateFilePath(measurementsFileLocation)
        } yield FindOptimalPathQuery(
          measurementsFilePath,
          startingIntersection,
          endingIntersection
        )
    }

  private def validateFilePath(
      measurementsFilePath: String
  ): Either[InvalidInputError.InvalidMeasurementsDataFilePath, Path] = {
    Either.fromOption(
      Try(Paths.get(measurementsFilePath).toFile.toPath).toOption,
      InvalidInputError.InvalidMeasurementsDataFilePath(measurementsFilePath)
    )
  }

  private def validateIntersection(
      intersectionString: String
  ): Either[InvalidInputError.InvalidIntersection, Intersection] =
    Either.fromOption(
      Intersection.fromString(intersectionString),
      InvalidInputError.InvalidIntersection(intersectionString)
    )
}
