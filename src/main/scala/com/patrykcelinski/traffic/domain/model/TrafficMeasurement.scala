package com.patrykcelinski.traffic.domain.model

case class TrafficMeasurement(
    measurementTime: Long,
    measurements: List[TransitMeasurement]
)
