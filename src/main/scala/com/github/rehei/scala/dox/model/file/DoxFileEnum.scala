package com.github.rehei.scala.dox.model.file

import com.github.rehei.scala.dox.util.DoxIndexedRepository

class DoxFileEnum(prefix: Option[String]) extends DoxIndexedRepository {

  class DoxUniqueFile extends DoxIndexedHandle {
    def get() = {
      Some(DoxFile(prefix.map(_ + "-").getOrElse("") + name.split("\\.").last))
    }
  }

  def unique = {
    new DoxUniqueFile()
  }

}