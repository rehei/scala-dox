package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.control.DoxHandleBibliography
import com.github.rehei.scala.dox.control.DoxCacheBibliography
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotUniqueException
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import java.nio.file.Path
import java.nio.file.Files
import scala.collection.JavaConversions._
import com.github.rehei.scala.dox.control.DoxBibKeyCountMap
import com.github.rehei.scala.dox.control.DoxBibKeyScanner

class TestCaching {

  class OpenCache(target: Path) extends DoxCacheBibliography(target) {
    override def path(key: DoxBibKey) = {
      super.path(key)
    }
    override def lookupMemoryCache(key: DoxBibKey) = {
      super.lookupMemoryCache(key)
    }
    override def lookupPersistentCache(key: DoxBibKey) = {
      super.lookupPersistentCache(key)
    }
  }

  object Example extends DoxBibKeyEnum {
    val REINHARDT_2019 = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }
  }

  @Test
  def test() {

    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache1 = new OpenCache(path)
    val map1 = DoxBibKeyCountMap(DoxBibKeyScanner.create[Example.type].list())
    val handle1 = DoxHandleBibliography(cache1, map1)

    val cache2 = new OpenCache(path)
    val map2 = DoxBibKeyCountMap(DoxBibKeyScanner.create[Example.type].list())

    val handle2 = new DoxHandleBibliography(cache2, map2)

    handle1.append(Example.REINHARDT_2019)
    handle1.writeTo(fileSystem.getPath("/tmp/example1.bib"))

    assertContent(cache1.lookupMemoryCache(Example.REINHARDT_2019).get)
    assertContent(cache1.lookupPersistentCache(Example.REINHARDT_2019).get)

    assert(cache2.lookupMemoryCache(Example.REINHARDT_2019).isEmpty)
    assertContent(cache2.lookupPersistentCache(Example.REINHARDT_2019).get)
    assertContent(cache2.lookupMemoryCache(Example.REINHARDT_2019).get)

    handle2.writeTo(fileSystem.getPath("/tmp/example2.bib"))

    assertContent(cache2.lookupMemoryCache(Example.REINHARDT_2019).get)
    assertContent(cache2.lookupPersistentCache(Example.REINHARDT_2019).get)
  }

  protected def assertContent(content: String) {
    assert(content.contains("doi = {10.1016/j.procir.2019.03.022}"))
    assert(content.contains("author = {Heiner Reinhardt and Marek Weber and Matthias Putz}"))
    assert(content.contains("title = {A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing}"))
  }

}