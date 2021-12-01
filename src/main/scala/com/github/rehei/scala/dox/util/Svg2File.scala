package com.github.rehei.scala.dox.util

import java.nio.file.Path

import scala.collection.mutable.HashMap
import scala.xml.Xhtml

import com.github.rehei.scala.dox.control.DoxReferenceFactory
import com.github.rehei.scala.dox.model.DoxSvgFigure

class Svg2File(protected val baseDirectory: Path) {

  protected val nextID = DoxReferenceFactory("image")
  protected val prefix = "generated"

  def generate(figure: DoxSvgFigure) = {
    val file = target(figure)
    IOUtils.writeStringUnique(file, Xhtml.toXhtml(figure.image))
    file
  }

  protected def target(figure: DoxSvgFigure) = {
    val filename = figure.config.label.map(_.name + ".svg").getOrElse(generateName)
    baseDirectory.resolve(filename)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.filename().referenceID}.svg"
  }

}