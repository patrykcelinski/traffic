package com.patrykcelinski.traffic.domain.repository

import com.patrykcelinski.traffic.domain.model.Measurements

import java.nio.file.Path

trait ReadMeasurementsRepository[F[_]] {
  def getMeasurements(measurementsFilePath: Path): F[Option[Measurements]]
}
