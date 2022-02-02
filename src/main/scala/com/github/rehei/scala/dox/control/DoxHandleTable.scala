package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeSvg
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.DoxFileTable
import com.github.rehei.scala.dox.util.SerializeTable

case class DoxHandleTable(_targetTex: Path, _targetTexTable: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexTable = _targetTexTable.normalize()

  assume(targetTexTable.toString().startsWith(targetTex.toString()))

  protected val tableFileGen = new SerializeTable(targetTexTable)

  def serialize(table: DoxFileTable): String = {
    val nameTable = tableFileGen.generate(table).getFileName.toString()
    targetTex.relativize(targetTexTable.resolve(nameTable)).toString()
  }

}