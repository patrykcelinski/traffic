package com.patrykcelinski.traffic.domain.model

import com.patrykcelinski.traffic.testutils.FlatTest

class IntersectionTest extends FlatTest {

  it should "allow to parse intersection from correct string" in {
    Intersection.fromString("A2") shouldBe Some(
      Intersection(Avenue('A'), Street(2))
    )
  }

  it should "not allow to parse incorrectly intersection string - intersection is an empty sting" in {
    Intersection.fromString(
      ""
    ) shouldBe None
  }

  it should "not allow to parse incorrectly intersection string  intersection has no street" in {
    Intersection.fromString(
      "F"
    ) shouldBe None
  }

  it should "not allow to parse incorrectly intersection string - intersection has no flipped (an avenue swapped with street)" in {
    Intersection.fromString(
      "15E"
    ) shouldBe None
  }

  it should "not allow to parse incorrectly intersection string - intersection has no avenue - it is just number" in {
    Intersection.fromString(
      "13"
    ) shouldBe None
  }
}
