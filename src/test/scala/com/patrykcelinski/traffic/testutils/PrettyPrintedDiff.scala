package com.patrykcelinski.traffic.testutils

import org.scalactic.Prettifier

trait PrettyPrintedDiff {
  implicit val prettifier: Prettifier = (o: Any) => pprint.apply(o).plainText
}
