package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInput
import com.github.rehei.scala.dox.model.DoxViewModelTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexRenderingTable
import com.github.rehei.scala.dox.control.tex.TexRenderingStyle
import com.github.rehei.scala.dox.model.DoxViewModelTableSequence
import com.github.rehei.scala.dox.control.tex.TexRenderingTableSequence
import com.github.rehei.scala.dox.util.SerializeUtils

case class DoxHandleTable(target: DoxTarget, style: TexRenderingStyle) {

  protected val serialize = SerializeUtils(target, ".tex")

  def handle(view: DoxViewModelTable[_]) = {

    val content = new TexRenderingTable(view.model.transform(), false, style).createTableString()
    val file = DoxInputFile(content, view.label)
    val target = serialize.write(file)

    DoxInput(target, file.fileCaption)
  }

  def handle(view: DoxViewModelTableSequence) = {

    val content = new TexRenderingTableSequence(view.models, view.title, style).createTableString()
    val file = DoxInputFile(content, view.label)
    val target = serialize.write(file)

    DoxInput(target, file.fileCaption)
  }
  
}