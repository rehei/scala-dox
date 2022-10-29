package com.github.rehei.scala.dox.util

import java.nio.file.Path

import com.github.rehei.scala.dox.model.DoxViewModelEquation
import com.github.rehei.scala.dox.model.DoxInputFile

class SerializeEquation(baseDirectory: Path) extends SerializeBase(baseDirectory, "equation") {

  def generate(equation: DoxInputFile) = {
    super.write(equation.content, equation.fileLabel, ".tex")
  }

}