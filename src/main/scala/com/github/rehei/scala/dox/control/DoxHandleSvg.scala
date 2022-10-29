package com.github.rehei.scala.dox.control

import java.nio.file.Path

import scala.xml.Xhtml

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode

case class DoxHandleSvg(mode: SvgMode, _targetTex: Path, _targetTexSVG: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()

  protected val inkscape = new InkscapeUtils(mode, targetTexSVG)

  assume(targetTexSVG.toString().startsWith(targetTex.toString()))

  protected val svgFileGen = SerializeUtils(targetTexSVG, "image", ".svg")

  def serialize(view: DoxViewModelSvg) = {
    val foo = DoxInputFile(content(view), view.label)
    val nameSVG = svgFileGen.write(foo).getFileName.toString()
    val filename = FilenameUtils.removeExtension(nameSVG)
    targetTex.relativize(targetTexSVG.resolve(mode.file(filename)))
  }

  def transform() = {
    inkscape.transform()
  }

  protected def content(figure: DoxViewModelSvg) = {
    Xhtml.toXhtml(figure.image)
  }

}