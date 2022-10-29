package com.github.rehei.scala.dox.util

import java.nio.file.Path

import scala.xml.Xhtml

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.model.DoxInputFile

class SerializeSvg(baseDirectory: Path) extends SerializeBase(baseDirectory, "image") {

  def generate(equation: DoxInputFile) = {
    super.write(equation.content, equation.fileLabel, ".svg")
  }
  


}