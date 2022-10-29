package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.util.SerializeEquation
import com.github.rehei.scala.dox.model.DoxViewModelEquation
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.control.tex.TexRenderingEquation
import com.github.rehei.scala.dox.model.DoxInput

case class DoxHandleEquation(_targetTex: Path, _targetTexEquation: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexTable = _targetTexEquation.normalize()

  assume(targetTexTable.toString().startsWith(targetTex.toString()))

  protected val tableFileGen = new SerializeEquation(targetTexTable)

  def serialize(view: DoxViewModelEquation) = {
    val content = new TexRenderingEquation(view.equation).createEquationString()
    val input = DoxInputFile(content, view.label)
    val nameTable = tableFileGen.generate(input).getFileName.toString()

    val filename = targetTex.relativize(targetTexTable.resolve(nameTable)).toString()

    DoxInput(filename, input.fileCaption)

  }

}