package com.github.rehei.scala.dox.test.control

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.control.tex.TexRendering
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.DoxHandleBibliography
import com.github.rehei.scala.dox.control.DoxCacheBibliography
import com.github.rehei.scala.dox.control.DoxBibKeyCountMap
import com.github.rehei.scala.dox.control.DoxBibKeyScanner
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder

object TestDoxBibKeyCountMap {

  object TestKeys extends DoxBibKeyEnum {

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

}

class TestDoxBibKeyCountMap {

  import TestDoxBibKeyCountMap._

  @Test
  def test() = {

    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")

    val cache = DoxCacheBibliography(path)
    val map = DoxBibKeyCountMap(DoxBibKeyScanner.create[TestDoxBibKeyCountMap.type].list())

    val bibHandle = DoxHandleBibliography(cache, map)

    val rendering = new TexRendering(TexAST(), null, null, bibHandle)

    val sequence = map.listAll()

    testing(map).x(0).y(0)

    rendering.cite(TestKeys.X)
    testing(map).x(1).y(0)

    rendering.cite(TestKeys.X)
    testing(map).x(2).y(0)

    rendering.citet(TestKeys.X)
    testing(map).x(3).y(0)

    rendering.citep(TestKeys.X)
    testing(map).x(4).y(0)

    rendering.cite(TestKeys.Y)
    testing(map).x(4).y(1)

    rendering.citet(TestKeys.Y)
    testing(map).x(4).y(2)

    rendering.citep(TestKeys.Y)
    testing(map).x(4).y(3)
  }

  protected def testing(map: DoxBibKeyCountMap) = new {
    def x(countX: Int) = new {
      def y(countY: Int) = {
        assert(map.listAll().filter(m => m.key == TestKeys.X && m.count == countX).size == 1)
        assert(map.listAll().filter(m => m.key == TestKeys.Y && m.count == countY).size == 1)
        assert(map.listAll().size == 2)
        assert(map.listAll().size == 2)
        assert(map.listZero().size == Seq(countX, countY).filter(_ == 0).size)
        assert(map.listMoreThanZero().size == Seq(countX, countY).filter(_ > 0).size)
      }
    }
  }

}