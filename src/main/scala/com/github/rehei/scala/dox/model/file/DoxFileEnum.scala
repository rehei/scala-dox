package com.github.rehei.scala.dox.model.file

import com.github.rehei.scala.dox.util.DoxIndexedRepository

class DoxFileEnum extends DoxIndexedRepository {

  class DoxUniqueFile extends DoxIndexedHandle {
    def get() = {
      Some(DoxFile(name.split("\\.").last))
    }
  }

  def unique = {
    new DoxUniqueFile()
  }

}