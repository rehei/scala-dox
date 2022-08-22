package com.github.rehei.scala.dox.test.model.table

import com.github.rehei.scala.dox.util.ViewSupport
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeFactory
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.util.TexUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.io.Writer
import org.apache.commons.io.FileUtils
import com.github.rehei.scala.dox.control.tex.TexRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.control.DoxHandleSvgTex
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.control.tex.TexRenderingTable
import com.github.rehei.scala.dox.control.DoxHandleEquation
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.test.util.TestTexHandle

object TestTable extends ViewSupport {

  case class StationSetup(station: String, capacityMin: Double, capacityMax: Double, time: String)
  protected val handle = new TestTexHandle()

  def main(args: Array[String]): Unit = {

    val $ = handle.texMain

    $.table {
      _.label(None).data(generatess())
    }

    ViewEngine.show(handle.generate())

  }

  protected def generatess() = {

    val factory = new DoxTableKeyNodeFactory[StationSetup]()
    import factory._
    //    val asd = Seq(
    //      Node(_.name(TotalSymbol.STATION_CAPACITY_MAX).alignment(_.NUMERIC).width(1.5)).finalize(_.apply(_.capacityMax)),
    //      Node(_.name(TotalSymbol.STATION_CAPACITY_MIN).alignment(_.NUMERIC).width(1.5)).finalize(_.apply(_.capacityMin)))
    //
    val table = {

      Table
        .create(
          Root()
            .append(
              Node(_.name("Station(en)").alignment(_.CENTER.ROTATE)).append(
                IndexDefault.big(),
                Value(_.name("Name").alignment(_.RIGHT.ROTATE).width(1.5)).finalize(_.apply(_.station)))))
    }

    val testSeq = Seq(
      StationSetup("a", 1, 2, "12s"),
      StationSetup("a2", 12, 21, "22s"),
      StationSetup("a555", 561, 26.5, "555s"),
      StationSetup("a44", 212, 2.44, "44s"),
      StationSetup("a6", 654, 546.0, "6s"),
      StationSetup("a3", 154, 542, "234s"))

    table.addAll(testSeq)
    table

  }

}