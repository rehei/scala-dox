package com.github.rehei.scala.dox.util

import java.nio.file.Path

import scala.xml.Xhtml

import com.github.rehei.scala.dox.model.DoxSvgFigure

class Svg2File(protected val baseDirectory: Path) {

  protected val nextID = NextID("image")
  protected val prefix = "generated"

  def generate(figure: DoxSvgFigure) = {
    val file = target(figure)
    IOUtils.writeStringUnique(file, Xhtml.toXhtml(figure.image))
    file
  }

  protected def target(figure: DoxSvgFigure) = {
    val filename = figure.label.map(_.referenceID + ".svg").getOrElse(generateName)
    baseDirectory.resolve(filename)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.nextID()}.svg"
  }

}