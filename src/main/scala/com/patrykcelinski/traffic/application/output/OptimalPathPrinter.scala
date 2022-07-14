package com.patrykcelinski.traffic.application.output

import cats.effect.Sync
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.domain.model.{Intersection, OptimalPath}
import cats.effect.std.Console
import cats.effect.kernel.Sync
import cats.syntax.all._
import io.circe.Encoder
import io.circe.generic.semiauto._
import io.circe.syntax._

object OptimalPathPrinter {

  private implicit val intersectionPathEncoder: Encoder[Intersection] =
    Encoder.encodeString.contramap(
      _.toString
    )
  private implicit val optimalPathEncoder: Encoder[OptimalPath]       = deriveEncoder

  def print[F[_]: Console](optimalPath: OptimalPath): F[Unit] =
      Console[F].println(optimalPath.asJson.toString())
}
