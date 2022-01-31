package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

abstract class SerializeBase(baseDirectory: Path, naming: String) {

  protected val nextID = NextID(naming)

  protected def write(content: String, naming: Option[_ <: DoxReferenceBase], extension: String) = {
    val file = target(naming, extension: String)
    IOUtils.writeStringUnique(file, content)
    file
  }

  protected def target(reference: Option[_ <: DoxReferenceBase], extension: String) = {
    val filename = reference.map(_.hashID).getOrElse(generateName)
    baseDirectory.resolve(filename + extension)
  }

  protected def generateName() = {
    s"generated_${nextID.nextID()}"
  }

}