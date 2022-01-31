package com.github.rehei.scala.dox.test

import java.nio.file.Paths
import org.junit.Test
import scala.collection.mutable.HashMap
import com.github.rehei.scala.dox.model.DoxFileTable
import com.github.rehei.scala.dox.util.SerializeTable
import com.github.rehei.scala.dox.model.DoxSvgFigure
import scala.xml.NodeSeq
import com.github.rehei.scala.dox.util.SerializeSvg
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.nio.file.Path
import java.nio.file.FileSystem
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.util.FileAlreadyExistsException
import com.github.rehei.scala.dox.model.reference.DoxIndexedEnum
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.util.SerializeEquation
import com.github.rehei.scala.dox.model.DoxFileEquation

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
    val tableFile = DoxFileTable("somestringtable", fileEnum.doxTable.get())
    val testTableName = new SerializeTable(tmp)
    testTableName.generate(tableFile)
    testTableName.generate(tableFile)
  }

  @Test(expected = classOf[FileAlreadyExistsException])
  def secondTest() {
    val svgFile = DoxSvgFigure(NodeSeq.Empty, fileEnum.doxImage.get())
    val testSvgName = new SerializeSvg(tmp)
    testSvgName.generate(svgFile)
    testSvgName.generate(svgFile)
  }

  @Test(expected = classOf[FileAlreadyExistsException])
  def thirdTest() {
    val equationFile = DoxFileEquation("someequation", fileEnum.doxEquation.get())
    val testEquationName = new SerializeEquation(tmp)
    testEquationName.generate(equationFile)
    testEquationName.generate(equationFile)
  }
}