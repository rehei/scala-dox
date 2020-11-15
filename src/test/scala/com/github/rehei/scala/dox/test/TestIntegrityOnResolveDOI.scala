package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.ex.DoxBibKeyIntegrityException

class TestIntegrityOnResolveDOI {

  protected val DOI = "https://doi.org/10.1016/j.procir.2019.03.022"

  protected val DEFAULT_YEAR = 2019
  protected val DEFAULT_BY = "Heiner Reinhardt and Marek Weber and Matthias Putz"
  protected val DEFAULT_TITLE = "A Survey on Automatic Model Generation for Material Flow Simulation in Discrete Manufacturing"

  @Test
  def testDefault() {

    object Example extends DoxBibKeyEnum {
      val REINHARDT = {
        fromDOI(DOI).year(DEFAULT_YEAR).by(DEFAULT_BY).title(DEFAULT_TITLE)
      }
    }

    assert(Example.REINHARDT.lookup().resolveValidated().get().startsWith("@article{"))
  }

  @Test(expected = classOf[DoxBibKeyIntegrityException])
  def testYear() {

    object Example extends DoxBibKeyEnum {
      val REINHARDT = {
        fromDOI(DOI).year(DEFAULT_YEAR - 1).by(DEFAULT_BY).title(DEFAULT_TITLE)
      }
    }

    Example.REINHARDT.lookup().resolveValidated()
  }

  @Test(expected = classOf[DoxBibKeyIntegrityException])
  def testByNonMatching() {

    object Example extends DoxBibKeyEnum {
      val REINHARDT = {
        fromDOI(DOI).year(DEFAULT_YEAR).by("foobar").title(DEFAULT_TITLE)
      }
    }

    Example.REINHARDT.lookup().resolveValidated()
  }
  
  @Test
  def testByWeakMatching() {

    object Example extends DoxBibKeyEnum {
      val REINHARDT = {
        fromDOI(DOI).year(DEFAULT_YEAR).by("Reinhardt et al.").title(DEFAULT_TITLE)
      }
    }

    Example.REINHARDT.lookup().resolveValidated()
  }
  

  @Test(expected = classOf[DoxBibKeyIntegrityException])
  def testTitle() {

    object Example extends DoxBibKeyEnum {
      val REINHARDT = {
        fromDOI(DOI).year(DEFAULT_YEAR).by(DEFAULT_BY).title("foobar")
      }
    }

    Example.REINHARDT.lookup().resolveValidated()
  }

}