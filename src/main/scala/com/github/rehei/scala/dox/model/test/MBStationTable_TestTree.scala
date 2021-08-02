package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableConfigBuilder
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigSupport
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.model.tree.DoxNode
import com.github.rehei.scala.dox.model.tree.DoxLeaf

case class MBStationTable_TestTree() {

  case class StationSetup(station: String, capacityMin: Int, capacityMax: Int, time: String)

  def generate() = {

    val config = new DoxTableKeyConfigSupport(TestDoxTableStringConversion(false))
    val configRoot = config.apply(_.NONE)
    val configDefault = config.apply(_.name("PLACEHOLDER").alignment(_.RIGHT).dynamic(false))
    val query = new Query[StationSetup]

    val treeTable = {
      DoxNode(configRoot)
        .withTableConfig(DoxTableConfigBuilder.caption("TEST").indexing(false))
        .addNodes(
          DoxLeaf(configDefault.name("Station"), query.apply(_.station)),
          DoxNode(configDefault.name("Kapazität"))
            .addNodes(
              DoxLeaf(configDefault.name("min"), query.apply(_.capacityMin)),
              DoxLeaf(configDefault.name("max"), query.apply(_.capacityMax))),
          DoxLeaf(configDefault.name("T"), query.apply(_.time)))
    }

    val tableOverview = DoxTableNew[StationSetup](treeTable)

    val testSeq = Seq(
      StationSetup("a", 1, 2, "b"),
      StationSetup("a2", 12, 21, "b2"),
      StationSetup("a555", 561, 265, "b555"),
      StationSetup("a44", 212, 244, "b44"),
      StationSetup("a3", 154, 542, "b3"))

    testSeq.map(tableOverview.add)

    tableOverview

  }

}