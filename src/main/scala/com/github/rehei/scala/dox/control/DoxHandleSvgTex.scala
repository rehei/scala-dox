package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.SerializeTex
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.control.tex.TexRenderingSVG
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInput

case class DoxHandleSvgTex(_targetTex: Path, _targetTexSVG: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()
  assume(targetTexSVG.toString().startsWith(targetTex.toString()))

  protected val svgHandle = DoxHandleSvg(SvgMode.PDF, _targetTex, _targetTexSVG)

  def serialize(view: DoxViewModelSvg) = {

    val texFileGen = new SerializeTex(svgHandle._targetTexSVG)
    val include = svgHandle.serialize(view)

    val content = new TexRenderingSVG(view, include).generate()
    val file = DoxInputFile(content, view.label)
    val nameTex = texFileGen.generate(file).getFileName.toString()

    val filename = targetTex.relativize(targetTexSVG.resolve(nameTex)).toString()

    DoxInput(filename, file.fileCaption)

  }

  def transform() = {
    svgHandle.transform()
  }

}