package com.github.rehei.scala.dox.util

import com.github.rehei.scala.dox.model.table.DoxTableFile
import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.model.DoxEquationFile

class TexEquation2File(protected val baseDirectory: Path) {

  protected val prefix = "generated"
  protected val nextID = NextID("equation")

  def generate(equation: DoxEquationFile) = {
    val file = target(equation)
    IOUtils.writeStringUnique(file, equation.fileContent)
    file
  }

  protected def target(equation: DoxEquationFile) = {
    val filename = equation.label.map(_.referenceID + ".tex").getOrElse(generateName)
    baseDirectory.resolve(filename)
  }

  protected def generateName() = {
    s"${prefix}_${nextID.nextID()}.tex"
  }
}