package com.patrykcelinski.traffic.domain.repository

import com.patrykcelinski.traffic.domain.model.Measurements

trait MeasurementsReadRepository[F[_]] extends GetMeasurements[F]

trait GetMeasurements[F[_]] {
  def getMeasurements(): F[Measurements]
}
