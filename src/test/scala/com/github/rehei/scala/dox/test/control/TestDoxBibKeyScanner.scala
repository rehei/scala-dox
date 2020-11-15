package com.github.rehei.scala.dox.test.control

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.ex.DoxBibKeySourceObjectRequiredException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotValidException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFinalException

object TestDoxBibKeyScanner {
  object TestRepository {

    object TestRepositoryKeyFinal extends DoxBibKeyEnum {
      var variableKey = fromRAW("")
    }

    object TestRepositoryKeyValidation extends TestRepositoryKeyValidation

    object TestRepositoryKeyValidationNested extends DoxBibKeyEnum {
      object foo {
        val reference = fromRAW("")
      }
    }

    object TestRepositorySourceObjectRequirement extends TestRepositorySourceObjectRequirement

    class TestRepositoryKeyValidation extends DoxBibKeyEnum {
      val reference1 = fromRAW("")
    }

    class TestRepositorySourceObjectRequirement extends DoxBibKeyEnum {

    }

    object TestAggregated extends DoxBibKeyEnum {

      val reference1 = fromRAW("")

      object Inner extends DoxBibKeyEnum {

        val reference2 = fromRAW((""))
      }
    }
  }
}

class TestDoxBibKeyScanner {

  import TestDoxBibKeyScanner._
  
  @Test(expected = classOf[DoxBibKeySourceObjectRequiredException])
  def testObjectRequirement() {
    DoxBibKeyScanner.create[TestRepository.TestRepositorySourceObjectRequirement].list()
  }

  @Test
  def testObjectRequirementSatisfied() {
    DoxBibKeyScanner.create[TestRepository.TestRepositorySourceObjectRequirement.type].list()
  }

  @Test(expected = classOf[DoxBibKeyNotValidException])
  def testKeyValidation() {
    DoxBibKeyScanner.create[TestRepository.TestRepositoryKeyValidation.type].list()
  }

  @Test(expected = classOf[DoxBibKeyNotValidException])
  def testKeyValidationNested() {
    DoxBibKeyScanner.create[TestRepository.TestRepositoryKeyValidationNested.type].list()
  }

  @Test(expected = classOf[DoxBibKeyNotFinalException])
  def testKeyFinal() {
    DoxBibKeyScanner.create[TestRepository.TestRepositoryKeyFinal.type].list()
  }

  @Test
  def testSequence() {
    val result = DoxBibKeyScanner.create[TestRepository.TestAggregated.type].list()

    assert(result.contains(TestRepository.TestAggregated.reference1))
    assert(result.contains(TestRepository.TestAggregated.Inner.reference2))
  }

}