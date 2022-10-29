package com.github.rehei.scala.dox.test

import org.junit.Test

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.reference.DoxIndexedEnum
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.FileAlreadyExistsException

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
  def test() {
    val input = DoxInputFile("somestringtable", fileEnum.doxTable.get())
    val serialize = SerializeUtils(tmp, "anything", ".anything")
    serialize.write(input)
    serialize.write(input)
  }


}