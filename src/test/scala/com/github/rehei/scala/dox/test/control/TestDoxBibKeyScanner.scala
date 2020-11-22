package com.github.rehei.scala.dox.test.control

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFinalException

class TestDoxBibKeyScanner {

  @Test(expected = classOf[DoxBibKeyNotFinalException])
  def testKeyFinal() {

    object TestRepositoryKeyFinal extends DoxBibKeyEnum {
      var variableKey = fromRAW("")
    }

    DoxBibKeyScanner(TestRepositoryKeyFinal).list()
  }

  @Test
  def testSequence() {

    object TestAggregated extends DoxBibKeyEnum {

      val reference1 = fromRAW("")

      object Inner {
        val reference2 = fromRAW("")
      }
    }

    val result = DoxBibKeyScanner(TestAggregated).list()

    println(result.size)
    
    assert(result.contains(TestAggregated.reference1))
    assert(result.contains(TestAggregated.Inner.reference2))
  }

  @Test
  def testMethod() {

    object TestAggregated extends DoxBibKeyEnum {

      val foo = fromRAW("")

      def reference1: DoxIndexedHandle = fromRAW("")

    }

    val result = DoxBibKeyScanner(TestAggregated).list()

    assert(result.size == 1)
    assert(result.contains(TestAggregated.foo))
    assert(!result.contains(TestAggregated.reference1))

  }

}