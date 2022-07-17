package com.patrykcelinski.traffic.domain.model.graph

import com.patrykcelinski.traffic.domain.model.graph.cost.TotalCost

case class Path[T](totalCost: TotalCost, path: List[T])

sealed trait PathfindingError extends Product with Serializable
object PathfindingError {
  case object NoPath                  extends PathfindingError
  case object NotExistingStartingNode extends PathfindingError
  case object NotExistingEndingNode   extends PathfindingError

}
