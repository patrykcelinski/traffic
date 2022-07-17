package com.patrykcelinski.traffic.application.input

import cats.implicits._
import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.domain.model.{Avenue, IntersectionKey, Street}
import com.patrykcelinski.traffic.testutils.FlatTest

import java.nio.file.Path

class FindOptimalPathQueryTest extends FlatTest {

  it should "allow to parse correctly formatted query" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "A1", "A2")
    ) shouldBe FindOptimalPathQuery(
      measurementsFilePath = Path.of(path),
      startingIntersection = IntersectionKey.make(
        Avenue('A'),
        Street(1)
      ),
      endingIntersection = IntersectionKey.make(
        Avenue('A'),
        Street(2)
      )
    ).asRight
  }

  it should "allow to parse correctly formatted query with steet with multiple digits" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "Z13", "A222")
    ) shouldBe FindOptimalPathQuery(
      measurementsFilePath = Path.of(path),
      startingIntersection = IntersectionKey.make(
        Avenue('Z'),
        Street(13)
      ),
      endingIntersection = IntersectionKey.make(
        Avenue('A'),
        Street(222)
      )
    ).asRight
  }

  it should "not allow to parse incorrectly formatted query - starting intersection is an empty sting" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "", "A2")
    ) shouldBe InvalidInputError.InvalidIntersection("").asLeft
  }

  it should "not allow to parse incorrectly formatted query - ending intersection is an empty sting" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "A1", "")
    ) shouldBe InvalidInputError.InvalidIntersection("").asLeft
  }

  it should "not allow to parse incorrectly formatted query - starting intersection is has no street" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "S", "A2")
    ) shouldBe InvalidInputError.InvalidIntersection("S").asLeft
  }

  it should "not allow to parse incorrectly formatted query - ending intersection has no street" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "A1", "S")
    ) shouldBe InvalidInputError.InvalidIntersection("S").asLeft
  }

  it should "not allow to parse incorrectly formatted query - starting intersection is flipped (an avenue swapped with street)" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "1A", "A2")
    ) shouldBe InvalidInputError.InvalidIntersection("1A").asLeft
  }

  it should "not allow to parse incorrectly formatted query - ending intersection has no flipped (an avenue swapped with street)" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "A2", "1A")
    ) shouldBe InvalidInputError.InvalidIntersection("1A").asLeft
  }

  it should "not allow to parse incorrectly formatted query - starting intersection has no avenue - it is just number" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "11", "A2")
    ) shouldBe InvalidInputError.InvalidIntersection("11").asLeft
  }

  it should "not allow to parse incorrectly formatted query - ending intersection has no avenue - it is just number" in {
    val path = "/sample-file.json"
    FindOptimalPathQuery.fromArgs(
      args = List(path, "A2", "11")
    ) shouldBe InvalidInputError.InvalidIntersection("11").asLeft
  }

}
