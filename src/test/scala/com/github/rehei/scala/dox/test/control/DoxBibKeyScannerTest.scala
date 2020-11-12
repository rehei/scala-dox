package com.github.rehei.scala.dox.test.control

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.control.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.ex.DoxBibKeyObjectRequiredException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyInvalidException

class DoxBibKeyScannerTest {

  @Test(expected = classOf[DoxBibKeyObjectRequiredException])
  def testObjectRequirement() {
    new DoxBibKeyScanner().list[TestRepositoryObjectRequirement]
  }

  @Test
  def testObjectRequirementSatisfied() {
    new DoxBibKeyScanner().list[TestRepositoryObjectRequirement.type]
  }
  
  @Test(expected = classOf[DoxBibKeyInvalidException])
  def testKeyValidation() {
    new DoxBibKeyScanner().list[TestRepositoryKeyValidation.type]
  }

}