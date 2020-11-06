package com.github.rehei.scala.dox.util

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import org.apache.commons.io.FileUtils
import com.github.rehei.scala.dox.model.DoxLikeSVG
import scala.xml.Xhtml

class SVG2File(protected val baseDirectory: Path) {

  protected val nextID = NextID("image")
  protected val prefix = "generated"

  def generateSVGFile(image: DoxLikeSVG) = {
    val file = nextFile()
    write(file, image)
    file
  }

  protected def write(file: File, image: DoxLikeSVG) = {
    val content = Xhtml.toXhtml(image.generateSVG())
    FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, false)
  }

  protected def nextFile() = {
    val filename = s"${prefix}_${nextID.next()}.svg"
    baseDirectory.resolve(filename).toFile()
  }

}