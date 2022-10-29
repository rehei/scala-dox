package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.SerializeTex
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.control.tex.TexRenderingSVG
import com.github.rehei.scala.dox.util.InkscapeUtils

case class DoxHandleSvgTex(_targetTex: Path, _targetTexSVG: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()
  assume(targetTexSVG.toString().startsWith(targetTex.toString()))
  
  protected val mode = SvgMode.PDF
  
  protected val inkscape = new InkscapeUtils(mode, targetTexSVG)
  protected val svgHandle = DoxHandleSvg(mode, _targetTex, _targetTexSVG)

  def serialize(svg: DoxSvgFigure) = {

    val texFileGen = new SerializeTex(svgHandle._targetTexSVG)
    val svgData = new TexRenderingSVG(svg, svgHandle).generate()
    val nameTex = texFileGen.generate(svgData.content, svgData.filepath).getFileName.toString()

    targetTex.relativize(targetTexSVG.resolve(nameTex)).toString()
  }

  def transform() = {
    inkscape.transform()
  }

}