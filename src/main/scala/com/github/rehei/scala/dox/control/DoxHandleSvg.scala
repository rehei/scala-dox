package com.github.rehei.scala.dox.control

import java.nio.file.Path

import scala.xml.Xhtml

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxInputData
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInputTarget
import java.nio.file.Files

case class DoxHandleSvg(target: DoxTarget, mode: SvgMode) {

  protected val inkscape = new InkscapeUtils(mode, target.directory)
  protected val serialize = SerializeUtils(target, ".svg")

  protected val includeTarget = target.updateSubDirectory(mode.directory)

  def handle(input: DoxInputData) = {
    val file = serialize.write(input)

    val filename = FilenameUtils.removeExtension(file.target.filename())
    val filenameUpdate = mode.file(filename)

    DoxInputFile(file.reference, DoxInputTarget(includeTarget.relative(filenameUpdate)))
  }

  def transform() = {
    Files.createDirectories(includeTarget.directory)
    inkscape.transform()
  }

}