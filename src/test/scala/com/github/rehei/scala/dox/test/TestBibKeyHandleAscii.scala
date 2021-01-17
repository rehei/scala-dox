package com.github.rehei.scala.dox.test

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyValueRAW
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import org.junit.Test
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameAsciiException

class TestBibKeyHandleAscii {

  @Test(expected = classOf[DoxBibKeyNameAsciiException])
  def testAsciiNameAsciiException() {
    val handle = createBibTexHandle()
    handle.append(DoxBibKeyValueRAW("kalaycı2020", null))
  }

  protected def createBibTexHandle() = {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = DoxBibKeyCache(path)
    val map = DoxBibKeyCountMap(Seq.empty).strict(false)

    DoxBibKeyRendering(cache, map)
  }

}