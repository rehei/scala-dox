package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexRenderingSVG
import com.github.rehei.scala.dox.model.DoxInput
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode

case class DoxHandleSvgTex(_targetTex: Path, _targetTexSVG: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()
  assume(targetTexSVG.toString().startsWith(targetTex.toString()))

  protected val svgHandle = DoxHandleSvg(SvgMode.PDF, _targetTex, _targetTexSVG)

  def serialize(view: DoxViewModelSvg) = {

    val texFileGen = SerializeUtils(svgHandle._targetTexSVG, "tex", ".tex")
    val include = svgHandle.serialize(view)

    val content = new TexRenderingSVG(view, include).generate()
    val file = DoxInputFile(content, view.label)
    val nameTex = texFileGen.write(file).getFileName.toString()

    val filename = targetTex.relativize(targetTexSVG.resolve(nameTex)).toString()

    DoxInput(filename, file.fileCaption)

  }

  def transform() = {
    svgHandle.transform()
  }

}