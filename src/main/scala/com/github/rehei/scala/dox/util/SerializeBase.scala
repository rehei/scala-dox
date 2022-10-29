package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

abstract class SerializeBase(baseDirectory: Path, naming: String) {

  protected val nextID = NextID(naming)

  protected def write(content: String, naming: String, extension: String) = {
    val file = target(naming, extension: String)
    IOUtils.writeStringUnique(file, content)
    file
  }

  protected def target(naming: String, extension: String) = {
    baseDirectory.resolve(naming + extension)
  }

}