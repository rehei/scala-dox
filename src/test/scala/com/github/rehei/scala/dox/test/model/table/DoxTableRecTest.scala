package com.github.rehei.scala.dox.test.model.table

import scala.collection.mutable.ListBuffer

import org.junit.Test

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion
import com.github.rehei.scala.dox.model.test.DoxTableKeyConfig_test
import com.github.rehei.scala.dox.model.test.DoxNode
import com.github.rehei.scala.dox.model.test.DoxLeaf
import com.github.rehei.scala.dox.model.test.DoxTableLatex

class DoxTableRecTest {
  case class DoxConfigCounter(config: DoxTableKeyConfig_test, var counter: Int)
  case class StationSetup(station: String, capacityMin: Int, capacityMax: Int, time: String)

  @Test
  def test() {

    val tree = DoxNode(
      "StationSetup",
      Seq(
        DoxLeaf("Station", "station"),
        DoxNode(
          "Kapazität",
          Seq(
            DoxLeaf("max", "capacityMin"),
            DoxLeaf("min", "capacityMax"))),
        DoxLeaf("T", "time")))

    println(DoxTableLatex.makeItSo(tree))
  }

  protected def dataTest() = {
    val stationSetup = StationSetup("asd", 3, 5, "dsa")
    stationSetup.getClass.getDeclaredFields foreach { f =>
      f.setAccessible(true)
      println(f.getName)
      println(f.get(stationSetup))
    }
  }

  //
  //    petersTree
  //      .addLeaf("Station", "station")
  //      .addNode("Kapazität")
  //      .addChildren(
  //        _
  //          .addLeaf("max", "capacityMin")
  //          .addLeaf("min", "capacityMax"))
  //      .addLeaf("T", "time")
}