package com.github.rehei.scala.dox.model.reference

import com.github.rehei.scala.dox.util.DoxIndexedRepository

class DoxIndexedEnum(prefix: Option[String]) extends DoxIndexedRepository {

  case class MyIndexedHandle[T](callback: String => T) extends DoxIndexedHandle {
    def get() = {
      key(callback, name, None)
    }
  }

  def uniqueTable = {
    MyIndexedHandle(DoxReferencePersistentTable(_))
  }

  def uniqueImage = {
    MyIndexedHandle(DoxReferencePersistentImage(_))
  }

  def uniqueEquation = {
    MyIndexedHandle(DoxReferencePersistentEquation(_))
  }

  protected def key[T](callback: String => T, name: String, extension: Option[String]) = {
    Some(callback(prefix.map(_ + "-").getOrElse("") + name.split("\\.").last + extension.map(e => "_" + e).getOrElse("")))
  }

}