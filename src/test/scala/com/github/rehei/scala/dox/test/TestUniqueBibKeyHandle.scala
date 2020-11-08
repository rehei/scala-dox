package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.control.DoxCacheBibliography
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import java.nio.file.Files
import com.github.rehei.scala.dox.control.DoxHandleBibliography
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotUniqueException

class TestUniqueBibKeyHandle {

  @Test(expected = classOf[DoxBibKeyNotUniqueException])
  def testUniquePlain() {

    object Example extends DoxBibKeyEnum {
      val plain1 = fromRAW {
        """
      @book{anything,
        title={Strategische Positionierung in der Automobilbranche: der Einsatz von virtueller Produktentwicklung und Wertsch{\"o}pfungsnetzwerken},
        author={Tietze, Oliver},
        year={2003},
        publisher={Springer-Verlag}
      }
    """
      }

      val plain2 = fromRAW {
        """
      @book{foobar,
        title={Strategische Positionierung in der Automobilbranche: der Einsatz von virtueller Produktentwicklung und Wertsch{\"o}pfungsnetzwerken},
        author={Tietze, Oliver},
        year={2003},
        publisher={Springer-Verlag}
      }
      """
      }
    }

    val handle = createBibTexHandle()

    handle.append(Example.plain1)
    handle.append(Example.plain2)
  }

  @Test(expected = classOf[DoxBibKeyNotUniqueException])
  def testUniquePOI() {

    object Example extends DoxBibKeyEnum {

      val FOO = {
        fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
          .year(2018).by("Romy Müller and Lukas Oehm").title("Process industries versus discrete processing: how system characteristics affect operator tasks")
      }

      val BAR = {
        fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
          .year(2018).by("Romy Müller and Lukas Oehm").title("Process industries versus discrete processing: how system characteristics affect operator tasks")
      }

    }

    val handle = createBibTexHandle()

    handle.append(Example.FOO)
    handle.append(Example.BAR)

  }

  protected def createBibTexHandle() = {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = new DoxCacheBibliography(path)
    new DoxHandleBibliography(cache)
  }

}