package com.github.rehei.scala.dox.util

import java.nio.file.Path

import com.github.rehei.scala.dox.model.DoxFileEquation

class SerializeEquation(baseDirectory: Path) extends SerializeBase(baseDirectory, "equation") {

  def generate(equation: DoxFileEquation) = {
    super.write(equation.content, equation.label, ".tex")
  }

}