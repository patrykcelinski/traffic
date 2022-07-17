package com.patrykcelinski.traffic.application.serialization
import cats.implicits._
import com.patrykcelinski.traffic.domain.model._
import io.circe.Decoder
import io.circe.generic.semiauto._
import io.circe.generic.extras.semiauto.deriveUnwrappedDecoder
object MeasurementsCodecs {


  implicit val avenueDecoder: Decoder[Avenue] = Decoder.decodeChar.map(
    Avenue.apply
  )
  implicit val streetDecoder: Decoder[Street] =
    Decoder.decodeString.emap(stringInt =>
      Either.fromOption(
        Street.fromString(stringInt),
        s"Invalid Street format: $stringInt"
      )
    )

  implicit val secondsDecodes: Decoder[Seconds] = deriveUnwrappedDecoder

  implicit val transitMeasurementDecoder: Decoder[TransitMeasurement] =
    deriveDecoder
  implicit val trafficMeasurementDecoder: Decoder[TrafficMeasurement] =
    deriveDecoder
  implicit val measurementsDecoder: Decoder[Measurements]             = deriveDecoder

}
