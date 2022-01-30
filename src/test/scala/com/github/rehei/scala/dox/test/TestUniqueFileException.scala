package com.github.rehei.scala.dox.test

import java.nio.file.Paths
import org.junit.Test
import scala.collection.mutable.HashMap
import com.github.rehei.scala.dox.model.table.DoxTableFile
import com.github.rehei.scala.dox.util.TexTable2File
import com.github.rehei.scala.dox.model.DoxSvgFigure
import scala.xml.NodeSeq
import com.github.rehei.scala.dox.util.Svg2File
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.nio.file.Path
import java.nio.file.FileSystem
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.util.FileAlreadyExistsException
import com.github.rehei.scala.dox.model.reference.DoxIndexedEnum
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.util.TexEquation2File
import com.github.rehei.scala.dox.model.DoxEquationFile

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestUniqueFileException extends DoxIndexedEnum(None) {

  class TestNamingRepository(prefix: Option[String]) extends DoxIndexedEnum(prefix) {
    val doxTable = uniqueTable
    val doxImage = uniqueImage
    val doxEquation = uniqueEquation
  }
  protected val fileSystem = MemoryFileSystemBuilder.newEmpty().build()
  protected val inmemory = fileSystem.getPath("/mnt/inmemory/")
  protected val tmp = inmemory.resolve("./datax/datax-" + "abc")
  protected val TARGET = tmp.normalize()
  protected val TARGET_DUMMY = TARGET.resolve("dmy")
  protected val fileEnum = new TestNamingRepository(None)

  @Test(expected = classOf[FileAlreadyExistsException])
  def firstTest() {
    val tableFile = DoxTableFile("somestringtable", fileEnum.doxTable.get())
    val testTableName = new TexTable2File(tmp)
    testTableName.generate(tableFile)
    testTableName.generate(tableFile)
  }

  @Test(expected = classOf[FileAlreadyExistsException])
  def secondTest() {
    val svgFile = DoxSvgFigure(NodeSeq.Empty, fileEnum.doxImage.get(), None)
    val testSvgName = new Svg2File(tmp)
    testSvgName.generate(svgFile)
    testSvgName.generate(svgFile)
  }

  @Test(expected = classOf[FileAlreadyExistsException])
  def thirdTest() {
    val equationFile = DoxEquationFile("someequation", fileEnum.doxEquation.get())
    val testEquationName = new TexEquation2File(tmp)
    testEquationName.generate(equationFile)
    testEquationName.generate(equationFile)
  }
}