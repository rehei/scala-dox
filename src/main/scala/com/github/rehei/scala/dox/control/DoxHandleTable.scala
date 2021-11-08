package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.Svg2File
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.table.DoxTableFile
import com.github.rehei.scala.dox.util.TexTable2File

case class DoxHandleTable(_targetTex: Path, _targetTexTable: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexTable = _targetTexTable.normalize()

  assume(targetTexTable.toString().startsWith(targetTex.toString()), "targetTexSVG should be within targetTex")

  protected val tableFileGen = new TexTable2File(targetTexTable)

  def serialize(figure: DoxTableFile): String = {
    val nameTable = tableFileGen.generate(figure).getFileName.toString()

    targetTex.relativize(targetTexTable.resolve(nameTable)).toString()
  }

}