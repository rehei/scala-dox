package com.github.rehei.scala.dox.util

import java.nio.file.Path

import scala.xml.Xhtml

import com.github.rehei.scala.dox.model.DoxSvgFigure

class SerializeSvg(baseDirectory: Path) extends SerializeBase(baseDirectory, "image") {

  def generate(figure: DoxSvgFigure) = {
    super.write(content(figure), figure.label, ".svg")
  }
  
  protected def content(figure: DoxSvgFigure) = {
    Xhtml.toXhtml(figure.image)
  }

}