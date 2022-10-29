package com.github.rehei.scala.dox.control

import java.nio.file.Path

import scala.xml.Xhtml

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils

case class DoxHandleSvg(target: DoxTarget, mode: SvgMode) {

  protected val inkscape = new InkscapeUtils(mode, target.directory)
  protected val serialize = SerializeUtils(target, ".svg")

  def handle(input: DoxInputFile) = {
    val target = serialize.write(input)

    val filename = FilenameUtils.removeExtension(target.filename())
    val filenameUpdate = mode.file(filename)
    
    target.update(filenameUpdate)
  }

  def transform() = {
    inkscape.transform()
  }

}