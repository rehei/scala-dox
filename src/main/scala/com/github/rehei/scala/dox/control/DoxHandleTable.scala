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
import com.github.rehei.scala.dox.model.reference.DoxReferenceUtils
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

case class DoxHandleTable(target: DoxTarget, style: TexRenderingStyle) {

  protected val serialize = SerializeUtils(target, ".tex")
  protected val resolve = DoxReferenceUtils("generated-table")

  def handle(view: DoxViewModelTable[_]) = {
    val content = new TexRenderingTable(view.model.transform(), false, style).createTableString()
    handleContent(view.label, content)
  }

  def handle(view: DoxViewModelTableSequence) = {
    val content = new TexRenderingTableSequence(view.models, view.title, style).createTableString()
    handleContent(view.label, content)
  }

  protected def handleContent(label: Option[DoxReferenceBase], content: String) = {
    val file = DoxInputFile(content, resolve.transform(label))
    val target = serialize.write(file)

    DoxInput(target.asString(), file.name)
  }

}