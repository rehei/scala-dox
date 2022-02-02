package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeSvg
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.DoxFileTex
import com.github.rehei.scala.dox.util.SerializeTex

case class DoxHandleTex(_targetTex: Path, _targetTexTex: Path) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexTex = _targetTexTex.normalize()

  assume(targetTexTex.toString().startsWith(targetTex.toString()))

  protected val texFileGen = new SerializeTex(targetTexTex)

  def serialize(tex: DoxFileTex): String = {
    val nameTex = texFileGen.generate(tex).getFileName.toString()
    targetTex.relativize(targetTexTex.resolve(nameTex)).toString()
  }

}