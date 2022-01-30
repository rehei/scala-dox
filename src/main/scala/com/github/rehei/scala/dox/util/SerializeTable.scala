package com.github.rehei.scala.dox.util

import java.nio.file.Path

import com.github.rehei.scala.dox.model.table.DoxTableFile

class SerializeTable(protected val baseDirectory: Path) {

  protected val prefix = "generated"
  protected val nextID = NextID("table")

  def generate(table: DoxTableFile) = {
    val file = target(table)
    IOUtils.writeStringUnique(file, table.filecontent)
    file
  }

  protected def target(table: DoxTableFile) = {
    val filename = table.label.map(_.hashID + ".tex").getOrElse(generateName)
    baseDirectory.resolve(filename)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.nextID()}.tex"
  }
}