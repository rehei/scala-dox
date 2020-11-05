package com.github.rehei.scala.dox.util

import java.nio.file.Path
import scala.sys.process.Process

class TEX2PDF(target: Path, filename: String) {

  //"pdflatex --shell-escape -synctex=1 -interaction=nonstopmode template.tex %.tex"

  protected val command = {
    "pdflatex -interaction=nonstopmode " + filename + ".tex %.tex"
  }

  protected val bibtex = {
    "bibtex " + filename + ".aux"
  }

  def generate() = {
    val pdftexProcess = Process(command, target.toFile())
    val bibtexProcess = Process(bibtex, target.toFile())

    // Two runs necessary for creating table of contents

    pdftexProcess.! // first run creates aux file which includes the table of contents
    bibtexProcess.! //
    pdftexProcess.! // second run reads from the aux file
    pdftexProcess.! // second run reads from the aux file

    target.resolve(filename + ".pdf").toFile()
  }

}