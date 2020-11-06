package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxReference
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.reference.DoxBibKey
import scala.collection.Seq

abstract class DoxRenderingBase(val bibliography: DoxHandleBibliography) {

  def list(callback: DoxListBuilder => DoxListBuilder) {
    val result = callback(DoxListBuilder(this, Seq.empty))
    result.flush()
  }

  def citet(key: DoxBibKey) {
    bibliography.append(key)
    citet(key.name())
  }
  def citep(key: DoxBibKey) {
    bibliography.append(key)
    citep(key.name())
  }
  def cite(key: DoxBibKey) {
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
  def table(in: DoxTable): Unit
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