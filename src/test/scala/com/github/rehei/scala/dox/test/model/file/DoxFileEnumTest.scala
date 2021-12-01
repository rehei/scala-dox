package com.github.rehei.scala.dox.test.model.file

import org.junit.Test
import com.github.rehei.scala.dox.model.file.DoxIndexedEnum
import com.github.rehei.scala.dox.model.file.DoxReferencePersistentTable

class DoxFileEnumTest {

  @Test
  def test() {

    object Repository extends DoxIndexedEnum(None) {
      val foo = uniqueTable
    }

    val value = Repository.foo.get()
    assert(value == Some(DoxReferencePersistentTable("foo")))
  }

}