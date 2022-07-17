package com.patrykcelinski.traffic.domain.model.graph
import cats.implicits._
import com.patrykcelinski.traffic.domain.model.graph.cost.{PathCost, TotalCost}
import com.patrykcelinski.traffic.testutils.FlatTest

class GraphTest extends FlatTest {

  /*
     A1 -1-> A2
   */
  it should "return not existing node if start node does not exist" in {
    val edgeA1_A2    = Edge("A1", "A2", PathCost(1.0))
    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_A2
        ),
        "A2" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A3", "A2") shouldBe NotExistingNode("A3").asLeft
  }

  /*
       A1 -1-> A2
   */
  it should "return not existing node if end node does not exist" in {
    val edgeA1_A2    = Edge("A1", "A2", PathCost(1.0))
    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_A2
        ),
        "A2" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A1", "A3") shouldBe NotExistingNode("A3").asLeft
  }

  /*
       A1 no path A2
   */
  it should "return no path for graph with two disconnected nodes (technically it is not a graph)" in {
    val oneNodeGraph = new Graph[String](
      Map(
        "A1" -> Set.empty,
        "A2" -> Set.empty
      )
    )
    oneNodeGraph.findPath("A1", "A2") shouldBe NoPath.asLeft
  }

  /*
       A1 -1-> A2
   */
  it should "return path for simple case with two connected nodes" in {
    val edgeA1_A2    = Edge("A1", "A2", PathCost(1.0))
    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_A2
        ),
        "A2" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A1", "A2") shouldBe Path(
      totalCost = TotalCost(1),
      path = List("A1", "A2")
    ).asRight
  }

  /*
       A1 -1-> A2 -1-> A3
   */
  it should "return path for simple case with three connected nodes (its linked list basically)" in {
    val edgeA1_A2    = Edge("A1", "A2", PathCost(1.0))
    val edgeA2_A3    = Edge("A2", "A3", PathCost(1.0))
    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_A2
        ),
        "A2" -> Set(
          edgeA2_A3
        ),
        "A3" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A1", "A3") shouldBe Path(
      totalCost = TotalCost(2),
      path = List("A1", "A2", "A3")
    ).asRight
  }

  /*
     A1 -1-> B1
     |
     1
     |
     V
     A2 -1-> B2
   */
  it should "return path for simple case with four connected nodes - one of them is a dead end, another one leads to the goal" in {
    val edgeA1_A2    = Edge("A1", "A2", PathCost(1.0))
    val edgeA1_B1    = Edge("A1", "B1", PathCost(1.0))
    val edgeA2_B2    = Edge("A2", "B2", PathCost(1.0))
    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_A2,
          edgeA1_B1
        ),
        "A2" -> Set(
          edgeA2_B2
        ),
        "B1" -> Set.empty,
        "B2" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A1", "B2") shouldBe Path(
      totalCost = TotalCost(2),
      path = List("A1", "A2", "B2")
    ).asRight
  }

  /*
      A1 -10-> B1
      |        |
      1        1
      |        |
      V        V
      A2 -1-> B2
   */
  it should "return path for simple case with four connected nodes - they form square, but one of initial ways is much slower" in {
    val edgeA1_A2 = Edge("A1", "A2", PathCost(1.0))
    val edgeA1_B1 = Edge("A1", "B1", PathCost(10.0))
    val edgeA2_B2 = Edge("A2", "B2", PathCost(1.0))
    val edgeB1_B2 = Edge("B1", "B2", PathCost(1.0))

    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_A2,
          edgeA1_B1
        ),
        "A2" -> Set(
          edgeA2_B2
        ),
        "B1" -> Set(
          edgeB1_B2
        ),
        "B2" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A1", "B2") shouldBe Path(
      totalCost = TotalCost(2),
      path = List("A1", "A2", "B2")
    ).asRight
  }

  /*
      A1 -1-> B1
      |        |
      1        1
      |        |
      V        V
      A2 -10-> B2
   */
  it should "return path for simple case with four connected nodes - they form square, but one of final ways is much slower" in {
    val edgeA1_A2 = Edge("A1", "A2", PathCost(1.0))
    val edgeA1_B1 = Edge("A1", "B1", PathCost(1.0))
    val edgeA2_B2 = Edge("A2", "B2", PathCost(10.0))
    val edgeB1_B2 = Edge("B1", "B2", PathCost(1.0))

    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_A2,
          edgeA1_B1
        ),
        "A2" -> Set(
          edgeA2_B2
        ),
        "B1" -> Set(
          edgeB1_B2
        ),
        "B2" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A1", "B2") shouldBe Path(
      totalCost = TotalCost(2),
      path = List("A1", "B1", "B2")
    ).asRight
  }

  /*
      A1 -10-> B1 -10-> C1
      |         |       |
      10       10       10
      |         |       |
      V         V       V
      A2 -10-> B2 -10-> C2
      |         |       |
      10       10       10
      |         |       |
      V         V       V
      A3 -10-> B3 -10-> C3

      Expressway A1 -> B3 cost 5
   */
  it should "return path for graph with expressway - use expressway it is much faster" in {
    val edgeA1_B1 = Edge("A1", "B1", PathCost(10.0))
    val edgeB1_C1 = Edge("B1", "C1", PathCost(10.0))

    val edgeA1_A2 = Edge("A1", "A2", PathCost(10.0))
    val edgeB1_B2 = Edge("B1", "B2", PathCost(10.0))
    val edgeC1_C2 = Edge("C1", "C2", PathCost(10.0))

    val edgeA2_B2 = Edge("A2", "B2", PathCost(10.0))
    val edgeB2_C2 = Edge("B2", "C2", PathCost(10.0))

    val edgeA2_A3 = Edge("A2", "A3", PathCost(10.0))
    val edgeB2_B3 = Edge("B2", "B3", PathCost(10.0))
    val edgeC2_C3 = Edge("C2", "C3", PathCost(10.0))

    val edgeA3_B3 = Edge("A3", "B3", PathCost(10.0))
    val edgeB3_C3 = Edge("B3", "C3", PathCost(10.0))

    val edge_expressway_A1_B3 = Edge("A1", "B3", PathCost(5.0))

    val twoNodeGraph = new Graph[String](
      Map(
        "A1" -> Set(
          edgeA1_B1,
          edgeA1_A2,
          edge_expressway_A1_B3
        ),
        "A2" -> Set(
          edgeA2_B2,
          edgeA2_A3
        ),
        "A3" -> Set(
          edgeA3_B3
        ),
        "B1" -> Set(
          edgeB1_B2,
          edgeB1_C1
        ),
        "B2" -> Set(
          edgeB2_C2,
          edgeB2_B3
        ),
        "B3" -> Set(
          edgeB3_C3
        ),
        "C1" -> Set(
          edgeC1_C2
        ),
        "C2" -> Set(
          edgeC2_C3
        ),
        "C3" -> Set.empty
      )
    )
    twoNodeGraph.findPath("A1", "C3") shouldBe Path(
      totalCost = TotalCost(15),
      path = List("A1", "B3", "C3")
    ).asRight
  }

  it should "return path for an example graph" in {
    val edgeS_A = Edge("S", "A", PathCost(7.0))
    val edgeS_B = Edge("S", "B", PathCost(2.0))
    val edgeS_C = Edge("S", "C", PathCost(3.0))

    val edgeA_B = Edge("A", "B", PathCost(3.0))
    val edgeA_D = Edge("A", "D", PathCost(4.0))

    val edgeB_D = Edge("B", "D", PathCost(4.0))
    val edgeB_H = Edge("B", "H", PathCost(1.0))

    val edgeC_L = Edge("C", "L", PathCost(2.0))

    val edgeD_F = Edge("D", "F", PathCost(5.0))

    val edgeF_H = Edge("F", "H", PathCost(3.0))

    val edgeG_E = Edge("G", "E", PathCost(2.0))

    val edgeH_G = Edge("H", "G", PathCost(2.0))

    val edgeI_K = Edge("I", "K", PathCost(4.0))

    val edgeJ_K = Edge("I", "K", PathCost(4.0))

    val edgeK_E = Edge("K", "E", PathCost(5.0))

    val edgeL_I = Edge("L", "I", PathCost(4.0))
    val edgeL_J = Edge("L", "J", PathCost(6.0))

    val exampleGraph = new Graph[String](
      Map(
        "S" -> Set(
          edgeS_A,
          edgeS_B,
          edgeS_C
        ),
        "A" -> Set(
          edgeA_B,
          edgeA_D
        ),
        "B" -> Set(
          edgeB_D,
          edgeB_H
        ),
        "C" -> Set(
          edgeC_L
        ),
        "D" -> Set(
          edgeD_F
        ),
        "E" -> Set.empty,
        "F" -> Set(
          edgeF_H
        ),
        "G" -> Set(
          edgeG_E
        ),
        "H" -> Set(
          edgeH_G
        ),
        "I" -> Set(
          edgeI_K
        ),
        "J" -> Set(
          edgeJ_K
        ),
        "K" -> Set(
          edgeK_E
        ),
        "L" -> Set(
          edgeL_I,
          edgeL_J
        )
      )
    )
    exampleGraph.findPath("S", "E") shouldBe Path(
      totalCost = TotalCost(2 + 1 + 2 + 2),
      path = List("S", "B", "H", "G", "E")
    ).asRight
  }

}
