package com.github.rehei.scala.dox.control.noop

import com.github.rehei.scala.dox.model.table.DataTable
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.DoxReference
import com.github.rehei.scala.dox.control.DoxHandleBibliography
import com.github.rehei.scala.dox.RenderingBase

class RenderingNull(handle: DoxHandleBibliography) extends RenderingBase(handle) {

  def label(reference: DoxReference) = Unit
  def chapter(name: String) = Unit
  def section(name: String) = Unit
  def subsection(name: String) = Unit
  def subsubsection(name: String) = Unit
  def text(in: String) = Unit
  def ref(reference: DoxReference) = Unit
  def svg(chart: DoxSVGFigureSet) = Unit
  def table(in: DataTable) = Unit
  def clearpage() = Unit
  def list(itemSeq: Seq[String]) = Unit

  def citet(key: String) = Unit
  def citep(key: String) =  Unit
  def cite(key: String) = Unit

}