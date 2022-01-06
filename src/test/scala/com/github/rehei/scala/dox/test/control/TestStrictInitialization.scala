package com.github.rehei.scala.dox.test.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.control.tex.TexRendering
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.model.ex.DoxBibKeyCountStrictException
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import org.junit.Test

class TestStrictInitialization {

  object Test extends DoxBibKeyEnum {

    val X = {
      fromRAW {
        """
        @inproceedings{anything,
          title={Mathematical computations for linked data applications with openmath},
          author={Wenzel, Ken and Reinhardt, Heiner},
          booktitle={Proceedings of the 24th Workshop on OpenMath},
          pages={38--48},
          year={2012}
        }
        """
      }
    }

    val Y = {
      fromRAW {
        """
        @inbook{N-480204,
          author = {Reinhardt, Heiner and Singer, Adrian and Hutloff, David and Wenzel, Ken and Bolev, Dimitri},
          title = {Automatische Identifikation und Datenerfassung in der Fertigung von Hochleistungskomponenten},
          booktitle = {futureTEX Basisvorhaben Smart Factory},
          pages = {70-79},
          year = {2017},
          keywords = {Hochleistungskomponente, Auto-ID, RFID, Barcode, QR-Code, Markierung, Steuerung, Produktion},
        } 
        """
      }
    }
  }

  @Test(expected = classOf[DoxBibKeyCountStrictException])
  def testStrictInitExceptionCite() = {
    testStrictInitExceptionByCallback(_.cite(Test.X))
  }

  @Test(expected = classOf[DoxBibKeyCountStrictException])
  def testStrictInitExceptionCiteP() = {
    testStrictInitExceptionByCallback(_.citeP(Test.X))
  }

  @Test(expected = classOf[DoxBibKeyCountStrictException])
  def testStrictInitExceptionCiteT() = {
    testStrictInitExceptionByCallback(_.citeT(Test.X))
  }

  protected def testStrictInitExceptionByCallback(callback: TexRendering => Unit) = {
    val map = DoxBibKeyCountMap(Seq.empty)
    val rendering = createTexRendering(map)

    callback(rendering)
  }

  protected def createTexRendering(map: DoxBibKeyCountMap) = {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")

    val bibCache = DoxBibKeyCache(path)
    val bibHandle = DoxBibKeyRendering(bibCache, map)

    new TexRendering(TexAST(), true, null, null, null, bibHandle, null, null)
  }

}