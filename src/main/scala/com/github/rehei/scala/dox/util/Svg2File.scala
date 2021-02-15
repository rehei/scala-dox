package com.github.rehei.scala.dox.util

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxLikeSvg
import scala.xml.Xhtml
import com.github.rehei.scala.dox.control.DoxReferenceFactory
import com.github.rehei.scala.dox.model.DoxSvgFigure

class Svg2File(protected val baseDirectory: Path) {

  protected val nextID = DoxReferenceFactory("image")
  protected val prefix = "generated"

  def generate(image: DoxSvgFigure) = {
    val file = target(image)
    write(file, image)
    file
  }

  protected def write(path: Path, figure: DoxSvgFigure) = {
    val content = Xhtml.toXhtml(figure.image.generateSvg())
    IOUtils.writeString(path, content)
  }

  protected def target(figure: DoxSvgFigure) = {
    val filename = figure.config.naming.map(_.name + ".svg").getOrElse(generateName)
    baseDirectory.resolve(filename)
  }
  
  protected def generateName() = {
    s"${prefix}_${nextID.filename().referenceID}.svg"
  }

}