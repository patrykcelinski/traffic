package com.patrykcelinski.traffic.application.output

import com.patrykcelinski.traffic.domain.model.{Intersection, OptimalPath}
import cats.effect.std.Console
import cats.syntax.all._
import com.patrykcelinski.traffic.domain.model.graph.cost.TotalCost
import io.circe.Encoder
import io.circe.generic.semiauto._
import io.circe.syntax._

object OptimalPathPrinter {

  private implicit val intersectionPathEncoder: Encoder[Intersection] =
    Encoder.encodeString.contramap(
      _.toString
    )
  private implicit val totalTransitTime: Encoder[TotalCost]           =
    Encoder.encodeDouble.contramap(
      _.value
    )
  private implicit val optimalPathEncoder: Encoder[OptimalPath]       = deriveEncoder

  def print[F[_]: Console](optimalPath: OptimalPath): F[Unit] =
    Console[F].println(optimalPath.asJson.toString())
}
