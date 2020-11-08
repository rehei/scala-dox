package com.github.rehei.scala.dox.test

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.ex.DoxBibKeyIntegrityException

class TestIntegrityOnResolveDOI {

  protected val DOI = "https://doi.org/10.1007/s10111-018-0511-1"

  protected val DEFAULT_YEAR = 2018
  protected val DEFAULT_BY = "Romy Müller and Lukas Oehm"
  protected val DEFAULT_TITLE = "Process industries versus discrete processing: how system characteristics affect operator tasks"

  @Test
  def testDefault() {

    object Example extends DoxBibKeyEnum {
      val MUELLER = {
        fromDOI(DOI).year(DEFAULT_YEAR).by(DEFAULT_BY).title(DEFAULT_TITLE)
      }
    }

    assert(Example.MUELLER.lookup().resolve().startsWith("@article{"))
  }

  @Test(expected = classOf[DoxBibKeyIntegrityException])
  def testYear() {

    object Example extends DoxBibKeyEnum {
      val MUELLER = {
        fromDOI(DOI).year(DEFAULT_YEAR - 1).by(DEFAULT_BY).title(DEFAULT_TITLE)
      }
    }

    Example.MUELLER.lookup().resolve()
  }

  @Test(expected = classOf[DoxBibKeyIntegrityException])
  def testBy() {

    object Example extends DoxBibKeyEnum {
      val MUELLER = {
        fromDOI(DOI).year(DEFAULT_YEAR).by("foobar").title(DEFAULT_TITLE)
      }
    }

    Example.MUELLER.lookup().resolve()
  }

  @Test(expected = classOf[DoxBibKeyIntegrityException])
  def testTitle() {

    object Example extends DoxBibKeyEnum {
      val MUELLER = {
        fromDOI(DOI).year(DEFAULT_YEAR).by(DEFAULT_BY).title("foobar")
      }
    }

    Example.MUELLER.lookup().resolve()
  }

}