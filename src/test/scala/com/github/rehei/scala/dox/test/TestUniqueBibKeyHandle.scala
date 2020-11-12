package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.control.DoxCacheBibliography
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import java.nio.file.Files
import com.github.rehei.scala.dox.control.DoxHandleBibliography
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotUniqueException
import com.github.rehei.scala.dox.control.DoxBibKeyCountMap
import com.github.rehei.scala.dox.control.DoxBibKeyScanner

class TestUniqueBibKeyHandle {

  @Test(expected = classOf[DoxBibKeyNotUniqueException])
  def testUniquePlain() {

    object Example extends DoxBibKeyEnum {
      val plain1 = fromRAW {
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

      val plain2 = fromRAW {
        """
        @inproceedings{foobar,
          title={Mathematical computations for linked data applications with openmath},
          author={Wenzel, Ken and Reinhardt, Heiner},
          booktitle={Proceedings of the 24th Workshop on OpenMath},
          pages={38--48},
          year={2012}
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

      val REINHARDT_2019a = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }

      val REINHARDT_2019b = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }

    }

    val handle = createBibTexHandle()

    handle.append(Example.REINHARDT_2019a)
    handle.append(Example.REINHARDT_2019b)

  }

  protected def createBibTexHandle() = {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = DoxCacheBibliography(path, Seq.empty)
    val map = DoxBibKeyCountMap(Seq.empty).strict(false)

    DoxHandleBibliography(cache, map)
  }

}