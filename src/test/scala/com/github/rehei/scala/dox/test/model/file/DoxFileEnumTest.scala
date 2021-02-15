package com.github.rehei.scala.dox.test.model.file

import org.junit.Test
import com.github.rehei.scala.dox.model.file.DoxFileEnum
import com.github.rehei.scala.dox.model.file.DoxFile

class DoxFileEnumTest {

  @Test
  def test() {

    object Repository extends DoxFileEnum {
      val foo = unique
    }

    val value = Repository.foo.get()
    assert(value == Some(DoxFile("foo")))
  }

}