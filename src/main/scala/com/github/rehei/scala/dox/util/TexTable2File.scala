package com.github.rehei.scala.dox.util

import java.nio.file.Path

import scala.collection.mutable.HashMap

import com.github.rehei.scala.dox.control.DoxReferenceFactory
import com.github.rehei.scala.dox.model.table.DoxTableFile
import java.nio.file.Files

class TexTable2File(protected val baseDirectory: Path) {

  protected val prefix = "generated"
  protected val nextID = DoxReferenceFactory("table")

  def generate(table: DoxTableFile) = {
    val file = target(table)
    IOUtils.writeStringUnique(file, table.filecontent)
    file
  }

  protected def target(table: DoxTableFile) = {
    val filename = table.label.map(_.name + ".tex").getOrElse(generateName)
    baseDirectory.resolve(filename)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.filename().referenceID}.tex"
  }
}