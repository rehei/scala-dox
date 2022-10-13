package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.control.DoxBuilderTableSequence
import com.github.rehei.scala.dox.model.DoxTableViewModelSequence
import com.github.rehei.scala.dox.control.DoxBuilderTable
import com.github.rehei.scala.dox.control.DoxBuilderEquation
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.control.DoxBuilderSvg

class TexRenderingNull extends TexRendering(null, false, null, null, null, null, null, null) {

  override def label(reference: DoxReferenceText) = this
  override def chapter(name: String) = this
  override def section(name: String) = this
  override def subsection(name: String) = this
  override def subsubsection(name: String) = this

  override def bigskip() = this

  override def tableSequence(callback: DoxBuilderTableSequence.type => DoxTableViewModelSequence) = this
  override def table(callback: DoxBuilderTable.type => DoxTableViewModel[_]): this.type = this
  override def equation(callback: DoxBuilderEquation.type => DoxEquation) = this
  override def svg(callback: DoxBuilderSvg.type => DoxSvgFigure) = this

  override def text(ast: TextAST) = this
  override def text(in: String) = this
  override def textItalic(in: String) = this
  override def textRed(in: String) = this

  override def ref(reference: DoxReferenceBase) = this

  override def clearpage() = this

  override def nonBreakingSpace = this


  override protected def internalPlain(in: String) = Unit

  override protected def internalCiteT(key: String) = Unit
  override protected def internalCiteP(key: String) = Unit
  override protected def internalCite(key: String) = Unit
  override protected def internalSvg(image: DoxSvgFigure) = Unit
  override protected def internalList(itemSeq: Seq[String]) = Unit

}