package com.patrykcelinski.traffic.domain.model

case class Avenue(id: Char) extends AnyVal {
  override def toString: String = id.toString
}
