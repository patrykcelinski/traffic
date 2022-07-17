package com.patrykcelinski.traffic.domain.model.graph

import com.patrykcelinski.traffic.domain.model.graph.cost.PathCost

// WeightedDirectedEdge
case class Edge[T](
    from: T,
    to: T,
    cost: PathCost
)
