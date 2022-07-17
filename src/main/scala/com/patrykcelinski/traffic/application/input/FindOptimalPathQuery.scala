package com.patrykcelinski.traffic.application.input

import cats.implicits._
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.domain.model.IntersectionKey

import java.nio.file.{Path, Paths}
import scala.util.Try

case class FindOptimalPathQuery private (
    measurementsFilePath: Path,
    startingIntersection: IntersectionKey,
    endingIntersection: IntersectionKey
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
  ): Either[InvalidInputError.InvalidIntersection, IntersectionKey] =
    Either.fromOption(
      IntersectionKey.fromString(intersectionString),
      InvalidInputError.InvalidIntersection(intersectionString)
    )
}
