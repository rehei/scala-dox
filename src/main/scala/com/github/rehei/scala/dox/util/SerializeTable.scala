package com.github.rehei.scala.dox.util

import java.nio.file.Path

import com.github.rehei.scala.dox.model.DoxInputFile

class SerializeTable(baseDirectory: Path) extends SerializeBase(baseDirectory, "table") {

  def generate(table: DoxInputFile) = {
    super.write(table.content, table.label, ".tex")
  }

}