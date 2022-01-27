package com.github.rehei.scala.dox.util

import java.nio.file.Path
import scala.sys.process.Process

class TexUtils(target: Path, filename: String, fastAndDirty: Boolean) {

  //"pdflatex --shell-escape -synctex=1 -interaction=nonstopmode template.tex %.tex"

  /*
  protected val command = {
    "pdflatex -interaction=nonstopmode " + filename + ".tex %.tex"
  }
  */

  protected val command = {
    "pdflatex " + filename + ".tex %.tex"
  }

  protected val bibtex = {
    "bibtex " + filename + ".aux"
  }

  def generatePDF() = {
    val pdftexProcess = Process(command, target.toFile())

    pdftexProcess.! // first run creates aux file which includes the table of contents

    if (!fastAndDirty) {
      generateBibTex()
      pdftexProcess.! // second run reads from the aux file
      pdftexProcess.! // third run might be necessary
    }

    target.resolve(filename + ".pdf").toFile()
  }

  protected def generateBibTex() {
    val bibtexProcess = Process(bibtex, target.toFile())
    bibtexProcess.! // run
  }

}