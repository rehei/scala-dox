package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.control.DoxTarget

case class SerializeUtils(target: DoxTarget, extension: String) {

  def write(input: DoxInputFile) = {
    val file = target.resolve(input.filename + extension)
    IOUtils.writeStringUnique(file, input.content)
    SerializeTarget(target.relative(file.getFileName.toString()))
  }

}