package com.patryk.celinski.traffic
import cats.implicits._
import cats.effect._
import com.patrykcelinski.traffic.testutils.FlatTest
import cats.effect.unsafe.implicits.global
import com.patrykcelinski.traffic.domain.model._
import com.patrykcelinski.traffic.domain.repository.ReadMeasurementsRepository
import com.patrykcelinski.traffic.infrastructure.filesystem.FileBasedReadMeasurementsRepository
import io.circe.DecodingFailure

import java.io.FileNotFoundException

class FileBasedReadMeasurementsRepositoryTest extends FlatTest {

  it should "be able of reading an existing resource file and returning deserialized measurements" in {
    Given("an existing source file in the correct format")
    val measurementsRepository: ReadMeasurementsRepository[IO] =
      new FileBasedReadMeasurementsRepository("test-data.json")

    When("reading measurements")
    Then("it should return deserialized measurements")
    measurementsRepository.getMeasurements().unsafeRunSync() shouldBe
    Measurements(
      trafficMeasurements = List(
        TrafficMeasurement(
          measurementTime = 83452,
          measurements = List(
            TransitMeasurement(
              startAvenue = Avenue('A'),
              startStreet = Street.fromString("1").get,
              transitTime = 59.57363899660943,
              endAvenue = Avenue('A'),
              endStreet = Street.fromString("2").get
            ),
            TransitMeasurement(
              startAvenue = Avenue('A'),
              startStreet = Street.fromString("2").get,
              transitTime = 40.753916740023314,
              endAvenue = Avenue('A'),
              endStreet = Street.fromString("3").get
            )
          )
        ),
        TrafficMeasurement(
          measurementTime = 83556,
          measurements = List(
            TransitMeasurement(
              startAvenue = Avenue('A'),
              startStreet = Street.fromString("2").get,
              transitTime = 8,
              endAvenue = Avenue('A'),
              endStreet = Street.fromString("3").get
            )
          )
        )
      )
    )
  }

  it can "raise exception if a source file does not exist" in {
    Given("the source file name is set to an not existing file")
    val measurementsRepository: ReadMeasurementsRepository[IO] =
      new FileBasedReadMeasurementsRepository("not-existing-data.json")

    When("reading measurements")
    Then("it should throw the file not found exception")
    assertThrows[FileNotFoundException](
      measurementsRepository.getMeasurements().unsafeRunSync()
    )
  }

  it can "raise exception if the resource file contains invalid data - Invalid Street, char instead of integer" in {

    Given(
      """the resource file with an invalid street - a letter is used instead of an integer: `"endStreet": "I"`."""
    )
    val measurementsRepository: ReadMeasurementsRepository[IO] =
      new FileBasedReadMeasurementsRepository("invalid-street-data.json")

    When("reading measurements")
    Then("it should throw the decoding failure exception")
    assertThrows[DecodingFailure](
      measurementsRepository.getMeasurements().unsafeRunSync()
    )
  }

}
