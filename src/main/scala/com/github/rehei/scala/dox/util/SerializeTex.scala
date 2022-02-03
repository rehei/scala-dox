package com.github.rehei.scala.dox.util

import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxFileTex
import com.github.rehei.scala.dox.model.DoxSvgFigure
import org.apache.commons.io.FilenameUtils

class SerializeTex(baseDirectory: Path) {

  def generate(content: String, svgPath: Path) = {
    val filePathString = FilenameUtils.removeExtension(svgPath.getFileName.toString()) + ".tex"
    val file = baseDirectory.resolve(filePathString)
    IOUtils.writeStringUnique(file, content)
    file
  }
}