package com.github.rehei.scala.dox.test.model.table

import org.junit.Test

import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion
import com.github.rehei.scala.dox.model.test.MBStationTable_TestTree
import com.github.rehei.scala.dox.model.test.TexRenderingTable_test
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

class DoxTableRecTest {
  case class DoxConfigCounter(config: DoxTableKeyConfig, var counter: Int)
  case class StationSetup(station: String, capacityMin: Int, capacityMax: Int, time: String)

  object Bla extends DoxTableStringConversion {
    def render(model: Any) = {
      "stringbla"
    }
  }

  @Test
  def test() {
    val test = MBStationTable_TestTree()
    val refTable = DoxReferenceTable("69")
    val floating = false
    val baseAST = TexAST()
    val testRend = new TexRenderingTable_test(baseAST, floating, test.generate(), refTable).create()
    
    println(baseAST.build())
  }
}
