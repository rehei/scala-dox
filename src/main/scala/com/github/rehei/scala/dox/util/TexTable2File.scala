package com.github.rehei.scala.dox.util

import java.nio.file.Path

import scala.collection.mutable.HashMap

import com.github.rehei.scala.dox.control.DoxReferenceFactory
import com.github.rehei.scala.dox.model.table.DoxTableFile

class TexTable2File(protected val baseDirectory: Path) {

  protected val usage = HashMap[String, Boolean]()
  protected val prefix = "generated"
  protected val nextID = DoxReferenceFactory("table")

  def generate(table: DoxTableFile) = {
    val file = target(table)
    write(file, table)

    file
  }

  protected def target(table: DoxTableFile) = {
    val filename = table.label.map(_.name + ".tex").getOrElse(generateName)
    assert(usage.get(filename).isEmpty)
    usage.put(filename, true)
    baseDirectory.resolve(filename)
  }

  protected def write(path: Path, table: DoxTableFile) = {
    val content = table.filecontent
    IOUtils.writeString(path, content)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.filename().referenceID}.tex"
  }
}