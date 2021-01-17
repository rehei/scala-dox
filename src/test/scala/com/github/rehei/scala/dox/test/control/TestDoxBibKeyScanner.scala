package com.github.rehei.scala.dox.test.control

import org.junit.Test
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner
import com.github.rehei.scala.dox.model.ex.DoxBibKeyFinalException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotFoundException

class TestDoxBibKeyScanner {

  @Test(expected = classOf[DoxBibKeyFinalException])
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

      object Nested {
        val reference2 = fromRAW("")

        object Nested {
          val reference3 = fromRAW("")
        }
      }
    }

    val result = DoxBibKeyScanner(TestAggregated).list()

    assert(result.size == 3)

    assert(result.contains(TestAggregated.reference1))
    assert(result.contains(TestAggregated.Nested.reference2))
    assert(result.contains(TestAggregated.Nested.Nested.reference3))
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