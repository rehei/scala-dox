package com.github.rehei.scala.dox.model.file

import com.github.rehei.scala.dox.util.DoxIndexedRepository

class DoxIndexedEnum(prefix: Option[String]) extends DoxIndexedRepository {

  case class MyIndexedHandle[T](callback: String => T) extends DoxIndexedHandle {
    def get() = {
      Some(callback(prefix.map(_ + "-").getOrElse("") + name.split("\\.").last))
    }
  }

  def uniqueTable = {
    MyIndexedHandle(DoxReferencePersistentTable(_))
  }

  def uniqueImage = {
    MyIndexedHandle(DoxReferencePersistentImage(_))
  }

}