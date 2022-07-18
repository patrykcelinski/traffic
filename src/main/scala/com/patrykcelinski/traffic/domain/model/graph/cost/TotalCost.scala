package com.patrykcelinski.traffic.domain.model.graph.cost

case class TotalCost(value: Double) extends AnyVal {
  def add(pathCost: PathCost): TotalCost                         =
    TotalCost(value + pathCost.value)
  def +(heuristicFunctionCost: HeuristicFunctionCost): TotalCost =
    TotalCost(value + heuristicFunctionCost.value)
  def <(other: TotalCost): Boolean                               = value < other.value
}

object TotalCost {
  val ZERO = TotalCost(0.0)
}
