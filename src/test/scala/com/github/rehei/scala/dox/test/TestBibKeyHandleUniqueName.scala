package com.github.rehei.scala.dox.test

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyValueRAW
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameAsciiException
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import org.junit.Assert
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameUniqueException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameUniqueException
import com.github.rehei.scala.dox.model.validation.DoxBibKeyValidationNameUnique

class TestBibKeyHandleUniqueName {

  @Test
  def testUniqueNameException() {

    object Example extends DoxBibKeyEnum {

      val REINHARDT_2019a = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }
    }

    val handle = createBibTexHandle()
    handle.append(Example.REINHARDT_2019a)

    try {
      handle.append(Example.REINHARDT_2019a)
      Assert.fail()
    } catch {
      case ex: DoxBibKeyNameUniqueException => // everything is ok
    }
  }

  protected def createBibTexHandle() = {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = DoxBibKeyCache(path)
    val map = DoxBibKeyCountMap(Seq.empty).strict(false)

    DoxBibKeyRendering(cache, map).validateUsing(DoxBibKeyValidationNameUnique())
  }

}