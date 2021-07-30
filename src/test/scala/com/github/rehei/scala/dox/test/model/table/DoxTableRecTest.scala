package com.github.rehei.scala.dox.test.model.table

import scala.collection.mutable.ListBuffer

import org.junit.Test

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion
import com.github.rehei.scala.dox.model.test.DoxTableKeyConfig_test
import com.github.rehei.scala.dox.model.test.DoxNode
import com.github.rehei.scala.dox.model.test.DoxLeaf
import com.github.rehei.scala.dox.model.test.DoxTableLatex
import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigSupport
import com.github.rehei.scala.dox.model.table.DoxTableFactoryKeySelection

class DoxTableRecTest {
  case class DoxConfigCounter(config: DoxTableKeyConfig_test, var counter: Int)
  case class StationSetup(station: String, capacityMin: Int, capacityMax: Int, time: String)

  object DoxTableConfigBuilder {
    def caption(caption: String) = new {
      def indexing(enableIndexing: Boolean) = {
        DoxTableConfig(caption, enableIndexing)
      }
    }
  }

  object Bla extends DoxTableStringConversion {
    def render(model: Any) = {
      "stringbla"
    }
  }

  @Test
  def test() {
    val config = new DoxTableKeyConfigSupport(Bla)
    val configDefault = config.apply(_.name("DEFAULT").alignment(_.CENTER).dynamic(false))

    val query = new Query[StationSetup]
    val blub = StationSetup("a", 1, 2, "b")

    val doxTree =
      DoxNode(config.apply(_.NONE))
        .addNodes(
          DoxLeaf(configDefault.name("Station"), query.apply(_.station)),
          DoxNode(configDefault.name("Kapazität"))
            .addNodes(
              DoxLeaf(configDefault.name("min"), query.apply(_.capacityMin)),
              DoxLeaf(configDefault.name("max"), query.apply(_.capacityMax))),
          DoxLeaf(configDefault.name("T"), query.apply(_.time)))

    //    config.rendering.render(value)

    DoxTableConfigBuilder.caption("Asd").indexing(true)
    println(DoxTableConfigBuilder)
    //    println(DoxTableLatex.makeItSo(tree))
  }
}
