package com.github.rehei.scala.dox.control

import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxInputFile

case class DoxTarget(protected val _baseDirectory: Path, protected val subDirectoryExtension: String) {

  protected val BASE = _baseDirectory.normalize()
  protected val targetSecondary = BASE.resolve(subDirectoryExtension).normalize()
  
  assume(targetSecondary.toString().startsWith(BASE.toString()))

  def resolve(input: DoxInputFile, extension: String) = {
    targetSecondary.resolve(input.fileLabel + extension)
  }

  def relative(given: String) = {
    BASE.relativize(targetSecondary.resolve(given))
  }

  def directory = {
    targetSecondary.normalize()
  }
}