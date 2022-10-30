package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.DoxInputData
import com.github.rehei.scala.dox.control.DoxTarget
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInputTarget

case class SerializeUtils(target: DoxTarget, extension: String) {

  def write(input: DoxInputData) = {
    val file = target.resolve(input.reference.filename + extension)
    IOUtils.writeStringUnique(file, input.content)
    val path = target.relative(file.getFileName.toString())
    DoxInputFile(input.reference, DoxInputTarget(path))
  }

}