package com.github.rehei.scala.dox.util

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import scala.collection.Seq
import scala.sys.process.Process
import org.apache.commons.io.FileUtils

class SVG2PDF(protected val baseDirectory: Path) {
  /*
   * Some inkscape examples for your convenience:
   * inkscape -D -z --file=" + basePath + ".svg --export-pdf=" + baseName + ".pdf									 // generates pdf vector image
   * inkscape -D -z --file=" + basePath + ".svg --export-pdf=" + baseName + ".pdf --export-latex   // generates pdf_tex + pdf-graphicelements
   * inkscape -D -z --file=" + basePath + ".svg --export-png=" + baseName + ".png									 // generates png raster image
   *
   * acronyms:
   *  -D => --export-area-drawing
   *  -z => --without-gui
   */

  def generate() = {
    generatePDFs()
  }

  protected def generatePDFs() = {
    Process(executable, baseDirectory.toFile()).!
  }

  protected def executable = {
    val commandFile = baseDirectory.resolve("shellscript.sh").toFile()
    FileUtils.writeStringToFile(commandFile, command, StandardCharsets.UTF_8, false)
    commandFile.setExecutable(true, true)
    commandFile.getAbsolutePath
  }

  protected def command = {

    val processors = Math.max((Runtime.getRuntime().availableProcessors() * 0.9).toInt, 1)
    
    Seq(
      "for f in *.svg; do echo --file=${f} --export-pdf=${f%.*}.pdf >> inkscape.command.txt",
      "done",
      "cat inkscape.command.txt | xargs -P" + processors + " -L1 inkscape --without-gui").mkString("\n")
  }

}