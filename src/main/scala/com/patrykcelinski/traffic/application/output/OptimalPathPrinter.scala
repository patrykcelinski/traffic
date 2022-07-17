package com.patrykcelinski.traffic.application.output

import cats.effect.Sync
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.domain.model.{IntersectionKey, OptimalPath}
import cats.effect.std.Console
import cats.effect.kernel.Sync
import cats.syntax.all._
import com.patrykcelinski.traffic.domain.model.graph.cost.TotalCost
import io.circe.Encoder
import io.circe.generic.semiauto._
import io.circe.syntax._

object OptimalPathPrinter {

  private implicit val intersectionPathEncoder: Encoder[IntersectionKey] =
    Encoder.encodeString.contramap(
      _.value
    )
  private implicit val totalTransitTime: Encoder[TotalCost]              =
    Encoder.encodeDouble.contramap(
      _.value
    )
  private implicit val optimalPathEncoder: Encoder[OptimalPath]          = deriveEncoder

  def print[F[_]: Console](optimalPath: OptimalPath): F[Unit] =
    Console[F].println(optimalPath.asJson.toString())
}
