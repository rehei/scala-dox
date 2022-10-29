package com.github.rehei.scala.dox.util

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils
import com.github.rehei.scala.dox.model.DoxInputFile

class SerializeTex(baseDirectory: Path) extends SerializeBase(baseDirectory, "tex") {

  def generate(table: DoxInputFile) = {
    super.write(table.content, table.fileLabel, ".tex")
  }

}