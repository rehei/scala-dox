package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeSvg
import com.github.rehei.scala.dox.util.SvgMode

case class DoxHandleSvg(mode: SvgMode, _targetTex: Path, _targetTexSVG: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()

  protected val inkscape = new InkscapeUtils(mode, targetTexSVG)

  assume(targetTexSVG.toString().startsWith(targetTex.toString()))

  protected val svgFileGen = new SerializeSvg(targetTexSVG)

  def serialize(figure: DoxSvgFigure): String = {
    val nameSVG = svgFileGen.generate(figure).getFileName.toString()
    val filename = FilenameUtils.removeExtension(nameSVG)
    targetTex.relativize(targetTexSVG.resolve(mode.file(filename))).toString()
  }

  def transform() = {
    inkscape.transform()
  }

  def filename(figure: DoxSvgFigure) = {
    val svgName = svgFileGen.targetFilename(figure).getFileName.toString()
    FilenameUtils.removeExtension(svgName)
  }

}