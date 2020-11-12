package com.github.rehei.scala.dox.test.control

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.control.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.ex.DoxBibKeySourceObjectRequiredException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotValidException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFinalException

class DoxBibKeyScannerTest {

  @Test(expected = classOf[DoxBibKeySourceObjectRequiredException])
  def testObjectRequirement() {
    new DoxBibKeyScanner().list[TestRepository.TestRepositorySourceObjectRequirement]
  }

  @Test
  def testObjectRequirementSatisfied() {
    new DoxBibKeyScanner().list[TestRepository.TestRepositorySourceObjectRequirement.type]
  }

  @Test(expected = classOf[DoxBibKeyNotValidException])
  def testKeyValidation() {
    new DoxBibKeyScanner().list[TestRepository.TestRepositoryKeyValidation.type]
  }

  @Test(expected = classOf[DoxBibKeyNotValidException])
  def testKeyValidationNested() {
    new DoxBibKeyScanner().list[TestRepository.TestRepositoryKeyValidationNested.type]
  }
  
  @Test(expected = classOf[DoxBibKeyNotFinalException])
  def testKeyFinal() {
    new DoxBibKeyScanner().list[TestRepository.TestRepositoryKeyFinal.type]
  }

  @Test
  def testSequence() {
    val result = new DoxBibKeyScanner().list[TestRepository.TestAggregated.type]
    
    assert(result.contains(TestRepository.TestAggregated.reference1))
    assert(result.contains(TestRepository.TestAggregated.Inner.reference2))
  }
  
}