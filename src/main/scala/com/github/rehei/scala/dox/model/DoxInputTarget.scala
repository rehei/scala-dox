package com.github.rehei.scala.dox.model

import java.nio.file.Path

case class DoxInputTarget(protected val _path: Path) {

  def update(filename: String) = {
    val pathUpdate = _path.getParent.resolve(filename)
    this.copy(pathUpdate)
  }

  def asString() = {
    _path.toString()
  }

  def filename() = {
    _path.getFileName().toString()
  }

}