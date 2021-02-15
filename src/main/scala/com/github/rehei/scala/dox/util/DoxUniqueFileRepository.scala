package com.github.rehei.scala.dox.util

import com.github.rehei.scala.dox.model.DoxFile
import scala.collection.mutable.HashMap

class DoxUniqueFileRepository extends DoxIndexedRepository {

  class DoxUniqueFile extends DoxIndexedHandle {
    def get() = {
      Some(DoxFile(name.split("\\.").last))
    }
  }

  def unique = {
    new DoxUniqueFile()
  }

}