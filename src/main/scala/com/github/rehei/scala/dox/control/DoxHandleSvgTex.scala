package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexRenderingSVG
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInputData
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils
import scala.xml.Xhtml
import scala.xml.NodeSeq
import com.github.rehei.scala.dox.model.DoxInputReference

case class DoxHandleSvgTex(target: DoxTarget, config: DoxHandleSvgConfig) {

  protected val svgHandle = DoxHandleSvg(target, SvgMode.PDF)
  protected val serialize = SerializeUtils(target, ".tex")
  protected val resolve = DoxReferenceUtils("generated-svg")

  def handle(view: DoxViewModelSvg) = {

    val reference = resolve.transform(view.label)

    val fileSvg = handleSvg(reference, view.image)
    val fileTex = handleTex(reference, fileSvg.target.asString())

    fileTex
  }

  def transform() = {
    svgHandle.transform()
  }

  protected def handleSvg(reference: DoxInputReference, data: NodeSeq) = {
    val input = DoxInputData(reference, content(data))
    svgHandle.handle(input)
  }

  protected def handleTex(reference: DoxInputReference, path: String) = {
    val content = new TexRenderingSVG(config, path).generate()
    val file = DoxInputData(reference, content)
    serialize.write(file)
  }

  protected def content(data: NodeSeq) = {
    Xhtml.toXhtml(data)
  }

}