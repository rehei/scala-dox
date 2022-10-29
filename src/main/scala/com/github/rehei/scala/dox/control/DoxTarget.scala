package com.github.rehei.scala.dox.control

import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxInputFile

case class DoxTarget(protected val _baseDirectory: Path, protected val subDirectoryExtension: String) {

  protected val BASE = _baseDirectory.normalize()
  protected val targetSecondary = BASE.resolve(subDirectoryExtension).normalize()
  
  assume(targetSecondary.toString().startsWith(BASE.toString()))

  def resolve(filename: String) = {
    targetSecondary.resolve(filename)
  }

  def relative(filename: String) = {
    BASE.relativize(targetSecondary.resolve(filename))
  }

  def directory = {
    targetSecondary.normalize()
  }
  
}