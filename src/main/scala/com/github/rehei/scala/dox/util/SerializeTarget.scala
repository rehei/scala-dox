package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxInput

case class SerializeTarget(protected val _path: Path) {

  def update(filename: String) = {
    
    val oldPath = _path
    val newPath = _path.getParent.resolve(filename)
    println(filename + "-->" + oldPath + "-->" + newPath.toString())
    
    this.copy(newPath)
  }
  
  def asString() = {
    _path.toString()
  }
  
  def filename() = {
    _path.getFileName().toString()
  }
  
}
