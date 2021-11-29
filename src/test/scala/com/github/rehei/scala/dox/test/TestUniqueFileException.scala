package com.github.rehei.scala.dox.test

import com.github.rehei.scala.dox.control.DoxReferenceFactory
import com.github.rehei.scala.dox.model.file.DoxFileEnum
import java.nio.file.Paths
import org.junit.Test
import scala.collection.mutable.HashMap
import com.github.rehei.scala.dox.model.table.DoxTableFile
import com.github.rehei.scala.dox.util.TexTable2File
import com.github.rehei.scala.dox.model.DoxSvgFigure
import scala.xml.NodeSeq
import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.util.Svg2File
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestUniqueFileException extends DoxFileEnum(None) {

  class TestNamingRepository(prefix: Option[String]) extends DoxFileEnum(prefix) {
    val doxFileName = unique
  }
  val inmemory = Paths.get("/mnt/inmemory/")
  val peter = unique
  val tmp = inmemory.resolve("./datax/datax-" + "abc")
  tmp.toFile().mkdirs()

  protected val TARGET = tmp.normalize()
  protected val TARGET_DUMMY = TARGET.resolve("dmy")
  protected val fileEnum = new TestNamingRepository(None)

  @Test(expected = classOf[AssertionError])
  def firstTest() {

    val tableFile = DoxTableFile("somestringtable", fileEnum.doxFileName.get())
    val testTableName = new TexTable2File(tmp)
    testTableName.generate(tableFile)
    testTableName.generate(tableFile)
  }
  @Test(expected = classOf[AssertionError])
  def secondTest() {
    val svgFile = DoxSvgFigure(DoxFigure("somecaption", fileEnum.doxFileName.get()), NodeSeq.Empty)
    val testSvgName = new Svg2File(tmp)
    testSvgName.generate(svgFile)
    testSvgName.generate(svgFile)
  }
}