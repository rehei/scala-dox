package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxReferenceEquation
import com.github.rehei.scala.dox.model.DoxReferenceLike
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.tree.DoxTableTree

class TexRenderingNull extends TexRendering(null, false, null, null, null, null) {

  override def label(reference: DoxReferenceLike) = this
  override def chapter(name: String) = this
  override def section(name: String) = this
  override def subsection(name: String) = this
  override def subsubsection(name: String) = this

  override def textItalic(in: String) = this
  override def textRed(in: String) = this

  override def ref(reference: DoxReferenceLike) = this
  override def table(reference: DoxReferenceTable, in: DoxTableTree[_]) = this
  override def eqnarray(label: DoxReferenceEquation, expression: String) = this
  override def clearpage() = this

  override def nonBreakingSpace = this

  override protected def internalText(in: String) = Unit
  override protected def internalPlain(in: String) = Unit

  override protected def internalCiteT(key: String) = Unit
  override protected def internalCiteP(key: String) = Unit
  override protected def internalCite(key: String) = Unit
  override protected def internalSvg(image: DoxSvgFigure) = Unit
  override protected def internalList(itemSeq: Seq[String]) = Unit

}