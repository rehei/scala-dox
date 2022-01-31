package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.util.SerializeEquation
import com.github.rehei.scala.dox.model.DoxFileEquation

case class DoxHandleEquation(_targetTex: Path, _targetTexEquation: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexTable = _targetTexEquation.normalize()

  assume(targetTexTable.toString().startsWith(targetTex.toString()))

  protected val tableFileGen = new SerializeEquation(targetTexTable)

  def serialize(equationFile: DoxFileEquation): String = {
    val nameTable = tableFileGen.generate(equationFile).getFileName.toString()

    targetTex.relativize(targetTexTable.resolve(nameTable)).toString()
  }

}