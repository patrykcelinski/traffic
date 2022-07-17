package com.patrykcelinski.traffic.domain.model

case class TransitMeasurement(
    startAvenue: Avenue,
    startStreet: Street,
    transitTime: Seconds,
    endAvenue: Avenue,
    endStreet: Street
)
