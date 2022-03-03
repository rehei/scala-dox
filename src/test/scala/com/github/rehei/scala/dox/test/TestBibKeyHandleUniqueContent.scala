package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import java.nio.file.Files
import com.github.rehei.scala.dox.model.ex.DoxBibKeyContentUniqueException
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyValueRAW
import com.github.rehei.scala.dox.test.util.Checking

class TestBibKeyHandleUniqueContent {

  object ExamplePOI extends DoxBibKeyEnum {

    val REINHARDT_2019a = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }

    val REINHARDT_2019b = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }

  }
  
  object ExamplePlain extends DoxBibKeyEnum {
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
  
  @Test()
  def testUniquePlain() {

    val handle = createBibTexHandle()
    handle.append(ExamplePlain.plain1)
    
    Checking.testException[DoxBibKeyContentUniqueException](() => handle.append(ExamplePlain.plain2))

  }

  @Test()
  def testUniquePOI() {

    val handle = createBibTexHandle()
    handle.append(ExamplePOI.REINHARDT_2019a)
    
    Checking.testException[DoxBibKeyContentUniqueException](() => handle.append(ExamplePOI.REINHARDT_2019b))

  }

  protected def createBibTexHandle() = {
    val fileSystem = MemoryFileSystemBuilder.newLinux().build()
    val path = fileSystem.getPath("/tmp/dox-bib-cache-test/")
    val cache = DoxBibKeyCache(path)
    val map = DoxBibKeyCountMap(Seq.empty).strict(false)

    DoxBibKeyRendering(cache, map)
  }

}