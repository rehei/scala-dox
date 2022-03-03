package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFoundException
import com.github.rehei.scala.dox.test.util.Checking

class TestNestedBibKeyReference {

  object Test extends DoxBibKeyEnum {

    object Example {
      val REINHARDT_2019 = {
        fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
          .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
      }

      object Inner {
        val REINHARDT_2019 = {
          fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
            .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
        }

        def BLI = {
          fromDOI("https://doi.org/10.1016/j.procir.2019.03.022")
            .year(2019).by("Heiner Reinhardt and Marek Weber and Matthias Putz").title("A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing")
        }

        val FOO = {
          BLI
        }
      }
    }

  }

  @Test
  def test1() {
    val tmp = Test.Example.REINHARDT_2019.lookup().resolveValidated().get()
    assert(tmp.startsWith("@article{com-github-rehei-scala-dox-test-TestNestedBibKeyReference-Test-Example-REINHARDT_2019-UUUUUUUUU-----"))
  }

  @Test
  def test2() {
    val tmp = Test.Example.Inner.REINHARDT_2019.lookup().resolveValidated().get()
    assert(tmp.startsWith("@article{com-github-rehei-scala-dox-test-TestNestedBibKeyReference-Test-Example-Inner-REINHARDT_2019-UUUUUUUUU-----"))
  }

  @Test()
  def test3() {
    Checking.testException[DoxBibKeyNotFoundException](() => Test.Example.Inner.BLI.lookup().resolveValidated().get())
  }

  @Test
  def test4() {
    val tmp = Test.Example.Inner.FOO.lookup().resolveValidated().get()
    assert(tmp.startsWith("@article{com-github-rehei-scala-dox-test-TestNestedBibKeyReference-Test-Example-Inner-FOO-UUU"))
  }

}