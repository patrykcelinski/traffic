package com.patrykcelinski.traffic.application.output

import cats.{Id, Show}
import cats.implicits._
import cats.effect.std.Console
import com.patrykcelinski.traffic.application.error.InvalidInputError
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

    printed.get shouldBe s"Invalid intersection: ${invalidIntersection}, should be in format \'{Avenue Char}{Street Integer}\' (regex: ^[A-Z]{1}\\d*$$) "
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

    printed.get shouldBe s"Invalid measurements file path: ${invalidMeasurementsDataFilePath}"
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

    printed.get shouldBe s"Measurements data file does not exit: ${notExistingMeasurementsDataFilePath.toString}"
  }

  def spyConsoleError[A](spy: String => Id[Unit]): Console[Id] =
    new Console[Id] {
      override def readLineWithCharset(charset: Charset): Id[String] = ???
      override def print[A](a: A)(implicit S: Show[A]): Id[Unit]     = ???
      override def println[A](a: A)(implicit S: Show[A]): Id[Unit]   = ???
      override def error[A](a: A)(implicit S: Show[A]): Id[Unit]     = spy(
        a.asInstanceOf[String]
      )
      override def errorln[A](a: A)(implicit S: Show[A]): Id[Unit]   = ???
    }

}
