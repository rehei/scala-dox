package com.github.rehei.scala.dox.control.noop

import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.DoxTableViewModelSequence
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.model.DoxEquation

class RenderingNull(handle: DoxBibKeyRendering) extends DoxRenderingBase(null, handle) {

  def label(reference: DoxReferenceText) = this
  def chapter(name: String) = this
  def section(name: String) = this
  def subsection(name: String) = this
  def subsubsection(name: String) = this

  def textItalic(in: String) = this
  def textRed(in: String) = this

  def ref(reference: DoxReferenceBase) = this

  //  def eqnarray(label: DoxReferencePersistentEquation, expression: String) = this
  def clearpage() = this

  def nonBreakingSpace = this

  protected def internalText(in: String) = Unit
  protected def internalPlain(in: String) = Unit

  protected def internalCiteT(key: String) = Unit
  protected def internalCiteP(key: String) = Unit
  protected def internalCite(key: String) = Unit
  protected def internalEquation(labelTable: DoxEquation) = Unit
  protected def internalTable(labelTable: DoxTableViewModel[_]) = Unit
  protected def internalTable(labelTable: DoxTableViewModelSequence) = Unit
  protected def internalSvg(image: DoxSvgFigure) = Unit
  protected def internalList(itemSeq: Seq[String]) = Unit

}