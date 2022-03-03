package com.github.rehei.scala.dox.test

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import org.junit.Test
import com.github.rehei.scala.dox.ExampleReference.REINHARDT

class TestSimple {
//Kein test!
  object Test extends DoxBibKeyEnum {
    val REINHARDT_2019 = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }
    
    val MÜLLER_2019 = {
      fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
        .year(2018).by("Müller, Oehm").title("Process industries versus discrete processing: how system characteristics affect operator tasks")
    }

    val WANG_2016 = {
      fromDOI("https://doi.org/10.1109/TASE.2015.2464234")
        .year(2016).by("Wang et al.").title("An overview of industrial alarm systems: Main causes for alarm overloading, research status, and open problems")
    }
    
  }

  @Test
  def test() {
    println(Test.REINHARDT_2019.lookup().resolveValidated().get())
    println(Test.MÜLLER_2019.lookup().resolveValidated().get())
    println(Test.WANG_2016.lookup().resolveValidated().get())
  }

}