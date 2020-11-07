package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyInvalidException

class TestNestedBibKeyReference {

  object Example extends DoxBibKeyEnum {
    val MUELLER = {
      fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
        .year(2018).by("Romy Müller and Lukas Oehm").title("Process industries versus discrete processing: how system characteristics affect operator tasks")
    }

    object Inner1 {
      val MUELLER = {
        fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
          .year(2018).by("Romy Müller and Lukas Oehm").title("Process industries versus discrete processing: how system characteristics affect operator tasks")
      }
    }

    object Inner2 extends DoxBibKeyEnum {
      val MUELLER = {
        fromDOI("https://doi.org/10.1007/s10111-018-0511-1")
          .year(2018).by("Romy Müller and Lukas Oehm").title("Process industries versus discrete processing: how system characteristics affect operator tasks")
      }
    }

  }

  @Test
  def test1() {
    val tmp = Example.MUELLER.lookup().resolve()
    assert(tmp.startsWith("@article{com-github-rehei-scala-dox-test-TestNestedBibKeyReference-Example--MUELLER-UUUUUUU"))
  }

  @Test(expected = classOf[DoxBibKeyInvalidException])
  def test2() {
    Example.Inner1.MUELLER.lookup().resolve()
  }

  @Test
  def test3() {
    val tmp = Example.Inner2.MUELLER.lookup().resolve()
    assert(tmp.startsWith("@article{com-github-rehei-scala-dox-test-TestNestedBibKeyReference-Example-Inner2--MUELLER-UUUUUUU"))
  }

}