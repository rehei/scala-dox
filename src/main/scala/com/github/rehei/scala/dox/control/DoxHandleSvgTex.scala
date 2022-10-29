package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexRenderingSVG
import com.github.rehei.scala.dox.model.DoxInput
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode

case class DoxHandleSvgTex(target: DoxTarget) {

  protected val svgHandle = DoxHandleSvg(target, SvgMode.PDF)
  protected val serialize = SerializeUtils(target, ".tex")

  def handle(view: DoxViewModelSvg) = {

    val include = svgHandle.handle(view)

    val content = new TexRenderingSVG(view, include.path).generate()
    val file = DoxInputFile(content, view.label)
    val target = serialize.write(file)

    DoxInput(target, file.fileCaption)
  }

  def transform() = {
    svgHandle.transform()
  }

}