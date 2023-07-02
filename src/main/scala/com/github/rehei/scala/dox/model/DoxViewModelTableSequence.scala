package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable
import com.github.rehei.scala.dox.control.tex.TexRenderingTableSequence
import com.github.rehei.scala.dox.control.tex.TexRenderingStyle
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.model.table.content.DoxContent.DoxLegend

case class DoxViewModelTableSequence(
  label:         Option[DoxReferencePersistentTable],
  tableSequence: DoxTableSequence,
  hintOption:    Option[DoxLegend],
  titleOption:   Option[TextAST])