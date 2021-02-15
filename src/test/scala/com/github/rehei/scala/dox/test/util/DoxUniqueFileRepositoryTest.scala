package com.github.rehei.scala.dox.test.util

import org.junit.Test
import com.github.rehei.scala.dox.util.DoxUniqueFileRepository
import com.github.rehei.scala.dox.model.DoxFile

class DoxUniqueFileRepositoryTest {

  @Test
  def test() {

    object Repository extends DoxUniqueFileRepository {
      val foo = unique
    }

    val value = Repository.foo.get()
    assert(value == Some(DoxFile("foo")))
  }

}