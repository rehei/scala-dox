package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeSvg
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.util.SerializeTable
import com.github.rehei.scala.dox.model.DoxInputFile
import com.github.rehei.scala.dox.model.DoxInput
import com.github.rehei.scala.dox.model.DoxViewModelTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexRenderingTable
import com.github.rehei.scala.dox.control.tex.TexRenderingStyle
import com.github.rehei.scala.dox.model.DoxViewModelTableSequence
import com.github.rehei.scala.dox.control.tex.TexRenderingTableSequence

case class DoxHandleTable(_targetTex: Path, _targetTexTable: Path, style: TexRenderingStyle) {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexTable = _targetTexTable.normalize()

  assume(targetTexTable.toString().startsWith(targetTex.toString()))

  protected val tableFileGen = new SerializeTable(targetTexTable)

  def serialize(view: DoxViewModelTable[_]) = {

    val content = new TexRenderingTable(view.model.transform(), false, style).createTableString()
    val file = DoxInputFile(content, view.label)
    val filename = serializeInput(file)

    DoxInput(filename, file.fileCaption)
  }

  def serialize(view: DoxViewModelTableSequence) = {

    val content = new TexRenderingTableSequence(view.models, view.title, style).createTableString()
    val file = DoxInputFile(content, view.label)
    val filename = serializeInput(file)

    DoxInput(filename, file.fileCaption)
  }
  
  
  def serializeInput(input: DoxInputFile): String = {
    val nameTable = tableFileGen.generate(input).getFileName.toString()
    targetTex.relativize(targetTexTable.resolve(nameTable)).toString()
  }

}