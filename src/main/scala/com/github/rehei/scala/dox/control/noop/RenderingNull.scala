package com.github.rehei.scala.dox.control.noop

import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxReferenceLike

class RenderingNull(handle: DoxBibKeyRendering) extends DoxRenderingBase(null, handle) {

  def label(reference: DoxReferenceLike) = this
  def chapter(name: String) = this
  def section(name: String) = this
  def subsection(name: String) = this
  def subsubsection(name: String) = this

  def text(in: String) = this
  def textItalic(in: String) = this

  def plain(in: String) = this

  def ref(reference: DoxReferenceLike) = this
  def table(in: DoxTable) = this
  def clearpage() = this

  def nonBreakingSpace = this

  def svg(chart: DoxSVGFigureSet) = Unit

  protected def citet(key: String) = Unit
  protected def citep(key: String) = Unit
  protected def cite(key: String) = Unit

  protected def list(itemSeq: Seq[String]) = Unit

}