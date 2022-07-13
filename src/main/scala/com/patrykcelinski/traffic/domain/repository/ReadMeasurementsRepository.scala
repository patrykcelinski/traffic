package com.patrykcelinski.traffic.domain.repository

import com.patrykcelinski.traffic.domain.model.Measurements

trait ReadMeasurementsRepository[F[_]] {
  def getMeasurements(): F[Measurements]
}
