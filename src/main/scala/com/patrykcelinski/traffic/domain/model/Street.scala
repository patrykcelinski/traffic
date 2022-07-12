package com.patrykcelinski.traffic.domain.model

case class Street private (id: Int) extends AnyVal

object Street {
  def fromString(str: String): Option[Street] =
    str.toIntOption.map(Street.apply)
}
