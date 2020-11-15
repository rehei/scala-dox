package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotValidException

class TestNestedBibKeyReference {

  object Example extends DoxBibKeyEnum {
    val REINHARDT_2019 = {
      fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
        .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
    }

    object Inner1 {
      val REINHARDT_2019 = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }
    }

    object Inner2 extends DoxBibKeyEnum {
      val REINHARDT_2019 = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }
    }

  }

  @Test
  def test1() {
    val tmp = Example.REINHARDT_2019.lookup().resolveValidated().get()
    assert(tmp.startsWith("@article{com-github-rehei-scala-dox-test-TestNestedBibKeyReference-Example--REINHARDT_2019-UUUUUUUUU-----"))
  }

  @Test(expected = classOf[DoxBibKeyNotValidException])
  def test2() {
    Example.Inner1.REINHARDT_2019.lookup().resolveValidated()
  }

  @Test
  def test3() {
    val tmp = Example.Inner2.REINHARDT_2019.lookup().resolveValidated().get()
    assert(tmp.startsWith("@article{com-github-rehei-scala-dox-test-TestNestedBibKeyReference-Example-Inner2--REINHARDT_2019-UUUUUUUUU-----"))
  }

}