package com.patrykcelinski.traffic.application.output

import cats.{Id, Show}
import cats.implicits._
import cats.effect.std.Console
import com.patrykcelinski.traffic.domain.model.graph.cost.TotalCost
import com.patrykcelinski.traffic.domain.model.{Intersection, OptimalPath}
import com.patrykcelinski.traffic.testutils.FlatTest
import java.nio.charset.Charset

class OptimalPathPrinterTest extends FlatTest {

  it should "print optimal path as json" in {
    val optimalPath = OptimalPath(
      startingIntersection = Intersection.fromString("A1").get,
      endingIntersection = Intersection.fromString("A2").get,
      path = List(
        "A1",
        "B1",
        "C1",
        "D1",
        "E1",
        "F1",
        "F2",
        "F3",
        "F4",
        "F5",
        "E5",
        "D5",
        "C5",
        "B5",
        "A5",
        "A4",
        "A3",
        "A2"
      ).map(str => Intersection.fromString(str).get),
      totalTransitTime = TotalCost(769.9560352603861)
    )

    var printed                       = none[String]
    val spy                           = (str: String) =>
      Id {
        printed = str.some
      }
    implicit val console: Console[Id] = spyConsolePrintln(spy)

    OptimalPathPrinter.print(optimalPath)

    printed.get shouldBe """{
                           |  "startingIntersection" : "A1",
                           |  "endingIntersection" : "A2",
                           |  "path" : [
                           |    "A1",
                           |    "B1",
                           |    "C1",
                           |    "D1",
                           |    "E1",
                           |    "F1",
                           |    "F2",
                           |    "F3",
                           |    "F4",
                           |    "F5",
                           |    "E5",
                           |    "D5",
                           |    "C5",
                           |    "B5",
                           |    "A5",
                           |    "A4",
                           |    "A3",
                           |    "A2"
                           |  ],
                           |  "totalTransitTime" : 769.9560352603861
                           |}""".stripMargin
  }

  def spyConsolePrintln[A](spy: String => Id[Unit]): Console[Id] =
    new Console[Id] {
      override def readLineWithCharset(charset: Charset): Id[String] = ???

      override def print[A](a: A)(implicit S: Show[A]): Id[Unit] = ???

      override def println[A](a: A)(implicit S: Show[A]): Id[Unit] = spy(
        a.asInstanceOf[String]
      )

      override def error[A](a: A)(implicit S: Show[A]): Id[Unit] = ???

      override def errorln[A](a: A)(implicit S: Show[A]): Id[Unit] = ???
    }

}
