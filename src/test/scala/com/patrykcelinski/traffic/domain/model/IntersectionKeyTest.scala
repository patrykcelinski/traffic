package com.patrykcelinski.traffic.domain.model

import com.patrykcelinski.traffic.testutils.FlatTest

class IntersectionKeyTest extends FlatTest {

  it should "allow to parse intersection from correct string" in {
    IntersectionKey.fromString("A2") shouldBe Some(
      IntersectionKey.make(Avenue('A'), Street(2))
    )
  }

  it should "not allow to parse incorrectly intersection string - intersection is an empty sting" in {
    IntersectionKey.fromString(
      ""
    ) shouldBe None
  }

  it should "not allow to parse incorrectly intersection string  intersection has no street" in {
    IntersectionKey.fromString(
      "F"
    ) shouldBe None
  }

  it should "not allow to parse incorrectly intersection string - intersection has no flipped (an avenue swapped with street)" in {
    IntersectionKey.fromString(
      "15E"
    ) shouldBe None
  }

  it should "not allow to parse incorrectly intersection string - intersection has no avenue - it is just number" in {
    IntersectionKey.fromString(
      "13"
    ) shouldBe None
  }
}
