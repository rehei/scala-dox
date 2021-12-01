package com.github.rehei.scala.dox.test

import com.github.rehei.scala.dox.model.file.DoxIndexedEnum
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
import java.nio.file.Path
import java.nio.file.FileSystem
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.util.FileAlreadyExistsException

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestUniqueFileException extends DoxIndexedEnum(None) {

  class TestNamingRepository(prefix: Option[String]) extends DoxIndexedEnum(prefix) {
    val doxTable = uniqueTable
    val doxImage = uniqueImage
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
    val svgFile = DoxSvgFigure(DoxFigure("somecaption", fileEnum.doxImage.get()), NodeSeq.Empty)
    val testSvgName = new Svg2File(tmp)
    testSvgName.generate(svgFile)
    testSvgName.generate(svgFile)
  }

}