package com.patrykcelinski.traffic.domain.repository

import com.patrykcelinski.traffic.application.error.InvalidInputError
import com.patrykcelinski.traffic.domain.model.Measurements

import java.nio.file.Path

trait ReadMeasurementsRepository[F[_]] {
  def getMeasurements(
      measurementsFilePath: Path
  ): F[Either[InvalidInputError, Measurements]]
}
