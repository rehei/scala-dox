package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.ExampleReference.REINHARDT
import com.github.rehei.scala.dox.model.ex.DoxBibKeyIntegrityException
import org.junit.Assert

class TestCacheValidation {

  object Example extends DoxBibKeyEnum {
    val REINHARDT_2019 = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }

    val REINHARDT_2019_INVALID = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2018).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }
  }

  @Test
  def test() = {

    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache1 = DoxBibKeyCache(path)
    val map1 = DoxBibKeyCountMap(DoxBibKeyScanner(Example).list())
    val handle1 = DoxBibKeyRendering(cache1, map1)

    handle1.append(Example.REINHARDT_2019)

    val cache2 = DoxBibKeyCache(path)
    val map2 = DoxBibKeyCountMap(DoxBibKeyScanner(Example).list())
    val handle2 = DoxBibKeyRendering(cache2, map2)

    try {
      cache2.lookupPersistentCacheValidated(Example.REINHARDT_2019_INVALID)
      Assert.fail()
    } catch {
      case ex: DoxBibKeyIntegrityException => //OK
    }

    assert(cache2.lookupPersistentCacheValidated(Example.REINHARDT_2019).isDefined)
  }

}