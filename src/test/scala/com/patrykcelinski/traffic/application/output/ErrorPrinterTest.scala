package com.patrykcelinski.traffic.application.output

import cats.{Id, Show}
import cats.implicits._
import cats.effect.std.Console
import com.patrykcelinski.traffic.application.error.{
  InvalidInputError,
  TrafficAppError
}
import com.patrykcelinski.traffic.domain.model.Intersection
import com.patrykcelinski.traffic.testutils.FlatTest

import java.nio.charset.Charset
import java.nio.file.Path

class ErrorPrinterTest extends FlatTest {

  it should "print message to the console in case of InvalidInputError.InvalidIntersection " in {
    var printed                       = none[String]
    val spy                           = (str: String) => Id { printed = str.some }
    implicit val console: Console[Id] = spyConsoleError(spy)

    val invalidIntersection = "11"

    ErrorPrinter
      .print(InvalidInputError.InvalidIntersection(invalidIntersection))

    printed.get shouldBe s"ERROR. Invalid intersection: ${invalidIntersection}, should be in format \'{Avenue Char}{Street Integer}\' (regex: ^[A-Z]{1}\\d*$$) "
  }

  it should "print message to the console in case of InvalidInputError.InvalidMeasurementsDataFilePath" in {
    var printed                       = none[String]
    val spy                           = (str: String) =>
      Id {
        printed = str.some
      }
    implicit val console: Console[Id] = spyConsoleError(spy)

    val invalidMeasurementsDataFilePath = ""

    ErrorPrinter
      .print(
        InvalidInputError.InvalidMeasurementsDataFilePath(
          invalidMeasurementsDataFilePath
        )
      )

    printed.get shouldBe s"ERROR. Invalid measurements file path: ${invalidMeasurementsDataFilePath}"
  }

  it should "print message to the console in case of InvalidInputError.NotExistingMeasurementsDataFilePath" in {
    var printed                       = none[String]
    val spy                           = (str: String) =>
      Id {
        printed = str.some
      }
    implicit val console: Console[Id] = spyConsoleError(spy)

    val notExistingMeasurementsDataFilePath = Path.of("/12312.txt")

    ErrorPrinter
      .print(
        InvalidInputError.NotExistingMeasurementsDataFilePath(
          notExistingMeasurementsDataFilePath
        )
      )

    printed.get shouldBe s"ERROR. Measurements data file does not exit: ${notExistingMeasurementsDataFilePath.toString}"
  }

  it should "print message to the console in case of TrafficAppError.ThereIsNoPathBetween" in {
    var printed                       = none[String]
    val spy                           = (str: String) =>
      Id {
        printed = str.some
      }
    implicit val console: Console[Id] = spyConsoleError(spy)
    val start                         = Intersection.fromString("A1").get
    val end                           = Intersection.fromString("A2").get
    ErrorPrinter
      .print(
        TrafficAppError.ThereIsNoPathBetween(start, end)
      )

    printed.get shouldBe s"ERROR. There is no path connecting $start and $end"
  }

  it should "print message to the console in case of TrafficAppError.IntersectionDoesNotExit" in {
    var printed                       = none[String]
    val spy                           = (str: String) =>
      Id {
        printed = str.some
      }
    implicit val console: Console[Id] = spyConsoleError(spy)
    val start                         = Intersection.fromString("A1").get
    ErrorPrinter
      .print(
        TrafficAppError.IntersectionDoesNotExit(start)
      )

    printed.get shouldBe s"ERROR. Given intersection $start does not exist"
  }

  it should "print message to the console in case of TrafficAppError.FileIsInWrongFormat" in {
    var printed                       = none[String]
    val spy                           = (str: String) =>
      Id {
        printed = str.some
      }
    implicit val console: Console[Id] = spyConsoleError(spy)
    ErrorPrinter
      .print(
        InvalidInputError.FileIsInWrongFormat
      )

    printed.get shouldBe s"ERROR. Data file is in the wrong format."
  }

  def spyConsoleError[A](spy: String => Id[Unit]): Console[Id] =
    new Console[Id] {
      override def readLineWithCharset(charset: Charset): Id[String] = ???
      override def print[A](a: A)(implicit S: Show[A]): Id[Unit]     = ???
      override def println[A](a: A)(implicit S: Show[A]): Id[Unit]   = ???
      override def error[A](a: A)(implicit S: Show[A]): Id[Unit]     = ???
      override def errorln[A](a: A)(implicit S: Show[A]): Id[Unit]   = spy(
        a.asInstanceOf[String]
      )
    }

}
