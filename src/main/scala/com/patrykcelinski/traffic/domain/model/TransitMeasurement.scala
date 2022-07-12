package com.patrykcelinski.traffic.domain.model

case class TransitMeasurement(
    startAvenue: Avenue,
    startStreet: Street,
    transitTime: Double,
    endAvenue: Avenue,
    endStreet: Street
)
