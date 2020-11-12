package com.github.rehei.scala.dox.test.control

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.ex.DoxBibKeySourceObjectRequiredException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotValidException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFinalException

class TestDoxBibKeyScanner {

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