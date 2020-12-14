package com.github.rehei.scala.dox.util

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxLikeSVG
import scala.xml.Xhtml
import com.github.rehei.scala.dox.control.DoxReferenceFactory

class SVG2File(protected val baseDirectory: Path) {

  protected val nextID = DoxReferenceFactory("image")
  protected val prefix = "generated"

  def generateSVGFile(image: DoxLikeSVG) = {
    val file = nextFile()
    write(file, image)
    file
  }

  protected def write(path: Path, image: DoxLikeSVG) = {
    val content = Xhtml.toXhtml(image.generateSVG())
    IOUtils.writeString(path, content)
  }

  protected def nextFile() = {
    val filename = s"${prefix}_${nextID.filename().referenceID}.svg"
    baseDirectory.resolve(filename)
  }

}