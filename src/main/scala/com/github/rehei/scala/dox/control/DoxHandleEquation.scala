package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexRenderingEquation
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInputData
import com.github.rehei.scala.dox.model.DoxViewModelEquation
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils

case class DoxHandleEquation(target: DoxTarget) {

  protected val serialize = SerializeUtils(target, ".tex")
  protected val resolve = DoxReferenceUtils("generated-eq")

  def handle(view: DoxViewModelEquation) = {
    val content = new TexRenderingEquation(view.equation).createEquationString()
    val input = DoxInputData(resolve.transform(view.label), content)
    serialize.write(input)
  }

}