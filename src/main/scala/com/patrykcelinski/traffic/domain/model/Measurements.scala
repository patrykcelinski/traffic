package com.patrykcelinski.traffic.domain.model

case class Measurements(
    trafficMeasurements: List[TrafficMeasurement]
) {
  def getRoutesTransitTimes(): List[RouteTransitTime] =
    trafficMeasurements.flatMap(
      _.measurements
        .map {
          case TransitMeasurement(
                startAvenue: Avenue,
                startStreet: Street,
                transitTime: Double,
                endAvenue: Avenue,
                endStreet: Street
              ) =>
            RouteTransitTime(
              Route(
                Intersection(startAvenue, startStreet),
                Intersection(endAvenue, endStreet)
              ),
              transitTime
            )
        }
    )
}
