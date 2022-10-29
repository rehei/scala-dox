package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexRenderingSVG
import com.github.rehei.scala.dox.model.DoxInput
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils
import scala.xml.Xhtml
import scala.xml.NodeSeq

case class DoxHandleSvgTex(target: DoxTarget) {

  protected val svgHandle = DoxHandleSvg(target, SvgMode.PDF)
  protected val serialize = SerializeUtils(target, ".tex")
  protected val resolve = DoxReferenceUtils("generated-svg")

  def handle(view: DoxViewModelSvg) = {

    val name = resolve.transform(view.label)

    val svg = handleSvg(name, view.image)
    val tex = handleTex(name, svg.asString(), view.titleOption)

    DoxInput(tex.asString(), name)
  }

  def transform() = {
    svgHandle.transform()
  }

  protected def handleSvg(name: String, data: NodeSeq) = {
    val input = DoxInputFile(content(data), name)
    svgHandle.handle(input)
  }

  protected def handleTex(name: String, path: String, titleOption: Option[String]) = {
    val content = new TexRenderingSVG(path, titleOption).generate()
    val file = DoxInputFile(content, name)
    serialize.write(file)
  }

  protected def content(data: NodeSeq) = {
    Xhtml.toXhtml(data)
  }

}