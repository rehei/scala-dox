package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexRenderingEquation
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInputData
import com.github.rehei.scala.dox.model.DoxViewModelEquation
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils
import com.github.rehei.scala.dox.model.DoxViewModelEquationPlain
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

case class DoxHandleEquation(target: DoxTarget) {

  protected val serialize = SerializeUtils(target, ".tex")
  protected val resolve = DoxReferenceUtils("generated-eq")

  def handle(view: DoxViewModelEquation) = {
    val content = new TexRenderingEquation(view.equation).createEquationString()
    handleContent(view.label, content)
  }

  def handle(view: DoxViewModelEquationPlain) = {
    handleContent(view.label, view.plain)
  }

  protected def handleContent(label: Option[DoxReferenceBase], content: String) = {
    val file = DoxInputData(resolve.transform(label), content)
    serialize.write(file)
  }

}