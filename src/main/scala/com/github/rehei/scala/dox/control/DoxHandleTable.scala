package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.DoxInputData
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxViewModelTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexRenderingTable
import com.github.rehei.scala.dox.control.tex.TexRenderingStyle
import com.github.rehei.scala.dox.model.DoxViewModelTableSequence
import com.github.rehei.scala.dox.control.tex.TexRenderingTableSequence
import com.github.rehei.scala.dox.util.SerializeUtils
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.DoxViewModelTablePlain

case class DoxHandleTable(target: DoxTarget, style: TexRenderingStyle) {

  protected val serialize = SerializeUtils(target, ".tex")
  protected val resolve = DoxReferenceUtils("generated-table")

  def handle(view: DoxViewModelTable[_]) = {
    val content = new TexRenderingTable(view.model.transform(), false, true, true, style).createTableString()
    handleContent(view.label, content)
  }

  def handle(view: DoxViewModelTableSequence) = {
    val content = new TexRenderingTableSequence(view.tableSequence, view.titleOption, view.hintOption, style).createTableString()
    handleContent(view.label, content)
  }

  def handle(view: DoxViewModelTablePlain) = {
    handleContent(view.label, view.content)
  }

  protected def handleContent(label: Option[DoxReferenceBase], content: String) = {
    val file = DoxInputData(resolve.transform(label), content)
    serialize.write(file)
  }

}