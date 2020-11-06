package com.github.rehei.scala.dox.control.noop

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.DoxReference
import com.github.rehei.scala.dox.control.DoxHandleBibliography
import com.github.rehei.scala.dox.DoxRenderingBase

class RenderingNull(handle: DoxHandleBibliography) extends DoxRenderingBase(handle) {

  def label(reference: DoxReference) = Unit
  def chapter(name: String) = Unit
  def section(name: String) = Unit
  def subsection(name: String) = Unit
  def subsubsection(name: String) = Unit
  def text(in: String) = Unit
  def ref(reference: DoxReference) = Unit
  def svg(chart: DoxSVGFigureSet) = Unit
  def table(in: DoxTable) = Unit
  def clearpage() = Unit
  def list(itemSeq: Seq[String]) = Unit

  def citet(key: String) = Unit
  def citep(key: String) =  Unit
  def cite(key: String) = Unit

}