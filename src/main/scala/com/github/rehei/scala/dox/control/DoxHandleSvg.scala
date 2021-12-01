package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.Svg2File
import com.github.rehei.scala.dox.util.SvgMode

case class DoxHandleSvg(mode: SvgMode, _targetTex: Path, _targetTexSVG: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()

  protected val inkscape = new InkscapeUtils(mode, targetTexSVG)

  assume(targetTexSVG.toString().startsWith(targetTex.toString()))

  protected val svgFileGen = new Svg2File(targetTexSVG)

  def serialize(figure: DoxSvgFigure): String = {
    val nameSVG = svgFileGen.generate(figure).getFileName.toString()
    val name = FilenameUtils.removeExtension(nameSVG)

    targetTex.relativize(targetTexSVG.resolve(mode.file(name))).toString()
  }
  
  def transform() = {
    inkscape.transform()
  }

}