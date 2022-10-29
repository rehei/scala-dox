package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeSvg
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.DoxInputFile
import scala.xml.Xhtml

case class DoxHandleSvg(mode: SvgMode, _targetTex: Path, _targetTexSVG: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()

  protected val inkscape = new InkscapeUtils(mode, targetTexSVG)

  assume(targetTexSVG.toString().startsWith(targetTex.toString()))

  protected val svgFileGen = new SerializeSvg(targetTexSVG)

  def serialize(view: DoxViewModelSvg) = {
    val foo = DoxInputFile(content(view), view.label)
    val nameSVG = svgFileGen.generate(foo).getFileName.toString()
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