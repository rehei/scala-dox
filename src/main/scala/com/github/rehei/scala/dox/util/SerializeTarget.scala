package com.github.rehei.scala.dox.util

import java.nio.file.Path

case class SerializeTarget(path: Path) {

  def update(filename: String) = {
    this.copy(path.getParent.resolve(filename))
  }

}
