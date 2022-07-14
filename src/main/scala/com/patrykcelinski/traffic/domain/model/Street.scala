package com.patrykcelinski.traffic.domain.model

case class Street (id: Int) extends AnyVal {
  override def toString: String =
    id.toString
}

object Street {
  def fromString(str: String): Option[Street] =
    str.toIntOption.map(Street.apply)
}
