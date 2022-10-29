package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.DoxInputFile

case class SerializeUtils(baseDirectory: Path, extendedDirectory: String, extension: String) {

  def write(input: DoxInputFile) = {
    val file = target(input.fileLabel)
    IOUtils.writeStringUnique(file, input.content)
    file
  }

  protected def target(naming: String) = {
    baseDirectory.resolve(naming + extension)
  }

}