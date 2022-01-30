package com.github.rehei.scala.dox.test.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexRendering
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum

class TestCountMap {

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

  @Test
  def test() = {

    val scanner = DoxBibKeyScanner(Test)
    val map = DoxBibKeyCountMap(scanner.list())

    val rendering = createTexRendering(map)

    testing(map).x(0).y(0)

    rendering.cite(Test.X)
    testing(map).x(1).y(0)

    rendering.cite(Test.X)
    testing(map).x(2).y(0)

    rendering.citeT(Test.X)
    testing(map).x(3).y(0)

    rendering.citeP(Test.X)
    testing(map).x(4).y(0)

    rendering.cite(Test.Y)
    testing(map).x(4).y(1)

    rendering.citeT(Test.Y)
    testing(map).x(4).y(2)

    rendering.citeP(Test.Y)
    testing(map).x(4).y(3)
  }

  protected def createTexRendering(map: DoxBibKeyCountMap) = {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")

    val bibCache = DoxBibKeyCache(path)
    val bibHandle = DoxBibKeyRendering(bibCache, map)

    new TexRendering(TexAST(), true, null, null, bibHandle, null, null)
  }

  protected def testing(map: DoxBibKeyCountMap) = new {
    def x(countX: Int) = new {
      def y(countY: Int) = {
        assert(map.listAll().filter(m => m.key == Test.X && m.count == countX).size == 1)
        assert(map.listAll().filter(m => m.key == Test.Y && m.count == countY).size == 1)
        assert(map.listAll().size == 2)
        assert(map.listAll().size == 2)
        assert(map.listZero().size == Seq(countX, countY).filter(_ == 0).size)
        assert(map.listMoreThanZero().size == Seq(countX, countY).filter(_ > 0).size)
      }
    }
  }

}