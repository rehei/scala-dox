package com.github.rehei.scala.dox.util

import java.nio.file.Path

import com.github.rehei.scala.dox.model.DoxFileTable

class SerializeTable(baseDirectory: Path) extends SerializeBase(baseDirectory, "table") {

  def generate(table: DoxFileTable) = {
    super.write(table.content, table.label, ".tex")
  }
  
}