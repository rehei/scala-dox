package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.util.TexEquation2File
import com.github.rehei.scala.dox.model.DoxEquationFile

case class DoxHandleEquation(_targetTex: Path, _targetTexEquation: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexTable = _targetTexEquation.normalize()

  assume(targetTexTable.toString().startsWith(targetTex.toString()))

  protected val tableFileGen = new TexEquation2File(targetTexTable)

  def serialize(equationFile: DoxEquationFile): String = {
    val nameTable = tableFileGen.generate(equationFile).getFileName.toString()

    targetTex.relativize(targetTexTable.resolve(nameTable)).toString()
  }

}