package com.patrykcelinski.traffic.application

import cats.implicits._
import cats.effect._

object TrafficApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    IO(println("Hello")) *> ExitCode.Success.pure[IO]
}
