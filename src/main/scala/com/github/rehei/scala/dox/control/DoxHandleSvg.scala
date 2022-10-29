package com.github.rehei.scala.dox.control

import java.nio.file.Path

import scala.xml.Xhtml

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.util.SvgMode

case class DoxHandleSvg(target: DoxTarget, mode: SvgMode) {

  protected val inkscape = new InkscapeUtils(mode, target.directory)

  protected val serialize = SerializeUtils(target, ".svg")

  def handle(view: DoxViewModelSvg) = {
    val input = DoxInputFile(content(view), view.label)
    val target = serialize.write(input)
    val filename = FilenameUtils.removeExtension(target.path.getFileName.toString())
    
    target.path.getParent.resolve(mode.file(filename))
  }

  def transform() = {
    inkscape.transform()
  }

  protected def content(figure: DoxViewModelSvg) = {
    Xhtml.toXhtml(figure.image)
  }

}