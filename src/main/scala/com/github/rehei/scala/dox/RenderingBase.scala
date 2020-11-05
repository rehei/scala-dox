package com.github.rehei.scala.dox

import com.github.rehei.scala.dox.control.DoxListBuilder
import com.github.rehei.scala.dox.control.DoxSVGBuilder
import com.github.rehei.scala.dox.control.DoxTextBuilder
import com.github.rehei.scala.dox.model.DoxReference
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.table.DataTable
import com.github.rehei.scala.dox.reference.ReferenceKey
import com.github.rehei.scala.dox.control.DoxHandleBibliography

abstract class RenderingBase(val bibliography: DoxHandleBibliography) {

  def list(callback: DoxListBuilder => DoxListBuilder) {
    val result = callback(DoxListBuilder(this, Seq.empty))
    result.flush()
  }

  def citet(key: ReferenceKey) {
    bibliography.append(key)
    citet(key.name())
  }
  def citep(key: ReferenceKey) {
    bibliography.append(key)
    citep(key.name())
  }
  def cite(key: ReferenceKey) {
    bibliography.append(key)
    cite(key.name())
  }

  protected def citet(key: String)
  protected def citep(key: String)
  protected def cite(key: String)

  def label(reference: DoxReference): Unit
  def chapter(name: String): Unit
  def section(name: String): Unit
  def subsection(name: String): Unit
  def subsubsection(name: String): Unit

  def text(in: String): Unit
  def text(callback: DoxTextBuilder => DoxTextBuilder) {
    val result = callback(DoxTextBuilder(this, Seq.empty))
    result.flush()
  }

  def ref(reference: DoxReference): Unit
  def table(in: DataTable): Unit
  def clearpage(): Unit
  def list(itemSeq: Seq[String])

  def svg(callback: DoxSVGBuilder.type => DoxSVGFigureSet) {
    val data = callback(DoxSVGBuilder)
    this.svg(data)
  }

  protected def svg(imageSetSequence: Seq[DoxSVGFigureSet]) {
    for (sequence <- imageSetSequence) {
      this.svg(sequence)
    }
  }

  protected def svg(imageSet: DoxSVGFigureSet)

}