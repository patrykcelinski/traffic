package com.patrykcelinski.traffic.testutils

import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait FlatTest
    extends AnyFlatSpec
    with Matchers
    with GivenWhenThen
    with PrettyPrintedDiff
