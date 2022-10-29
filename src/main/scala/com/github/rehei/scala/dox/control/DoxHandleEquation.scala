package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexRenderingEquation
import com.github.rehei.scala.dox.model.DoxInput
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelEquation
import com.github.rehei.scala.dox.util.SerializeUtils

case class DoxHandleEquation(target: DoxTarget) {

  protected val serialize = SerializeUtils(target, ".tex")

  def handle(view: DoxViewModelEquation) = {
    val content = new TexRenderingEquation(view.equation).createEquationString()
    val input = DoxInputFile(content, view.label)
    val target = serialize.write(input)

    DoxInput(target, input.fileCaption)

  }

}