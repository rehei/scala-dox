package com.github.rehei.scala.dox

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

import scala.collection.Seq
import scala.reflect.api.materializeWeakTypeTag

import org.apache.commons.io.FileUtils

import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeFactory
import com.github.rehei.scala.dox.util.TexUtils
import com.github.rehei.scala.dox.util.ViewSupport
import com.github.rehei.scala.dox.control.tex.TexRendering
import com.github.rehei.scala.dox.control.tex.TexRenderingStyle

object TestTableRotatedAlignment extends ViewSupport {

  case class TestTexHandle() {

    protected val BASE = Files.createTempDirectory("dox")

    protected val SOURCE = Paths.get("./src/main/resources/latex")
    protected val TARGET = BASE.resolve("./dashboard-rendering-latex")
    protected val TARGET_INCLUDE_MAIN = TARGET.resolve("./generated_main.tex")

    protected val tex = new TexUtils(TARGET, "document", false)
    protected val tableHandle = DoxHandleTable(TARGET, TARGET.resolve("tex-table"))
    protected val texMainAST = TexAST()

    val texMain = new TexRendering(texMainAST, false, null, null, tableHandle, null, null, TexRenderingStyle.FRAMED)

    def generate() = {
      write()
      tex.generatePDF()
    }

    protected def write() {
      FileUtils.copyDirectory(SOURCE.toFile(), TARGET.toFile())
      val writer = Files.newBufferedWriter(TARGET_INCLUDE_MAIN, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
      texMainAST.write(writer)
      writer.close()
    }

  }

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
    val table = {

      Table
        .create(
          Root()
            .append(
              Node(_.name("FOOBAR FOOBAR FOOBAR FOOBAR ").alignment(_.RIGHT.ROTATE)).append(
                Value(_.name("FOOBAR FOOBAR FOOBAR FOOBAR ").alignment(_.RIGHT.ROTATE).width(0.5)).finalize(_.apply(_.station)),
                Value(_.name("FOOBAR FOOBAR FOOBAR FOOBAR ").alignment(_.RIGHT.ROTATE).width(0.5)).finalize(_.apply(_.station)),
                Value(_.name("FOOBAR FOOBAR FOOBAR FOOBAR ").alignment(_.RIGHT.ROTATE).width(0.5)).finalize(_.apply(_.station)))))
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