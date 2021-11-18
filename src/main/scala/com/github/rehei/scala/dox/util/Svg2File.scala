package com.github.rehei.scala.dox.util

import java.nio.file.Path

import scala.collection.mutable.HashMap
import scala.xml.Xhtml

import com.github.rehei.scala.dox.control.DoxReferenceFactory
import com.github.rehei.scala.dox.model.DoxSvgFigure

class Svg2File(protected val baseDirectory: Path) {

  protected val nextID = DoxReferenceFactory("image")
  protected val prefix = "generated"

  protected val usage = HashMap[String, Boolean]()

  def generate(image: DoxSvgFigure) = {
    val file = target(image)
    write(file, image)
    file
  }

  protected def write(path: Path, figure: DoxSvgFigure) = {
    val content = Xhtml.toXhtml(figure.image)
    IOUtils.writeString(path, content)
  }

  protected def target(figure: DoxSvgFigure) = {
    val filename = figure.config.label.map(_.name + ".svg").getOrElse(generateName)
    assert(usage.get(filename).isEmpty)
    usage.put(filename, true)
    baseDirectory.resolve(filename)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.filename().referenceID}.svg"
  }

}