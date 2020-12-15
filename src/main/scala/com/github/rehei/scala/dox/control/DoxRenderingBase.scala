package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import scala.collection.Seq
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxReferenceFigure
import com.github.rehei.scala.dox.model.DoxReferenceLike
import com.github.rehei.scala.dox.model.DoxReferenceEquation
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.DoxDelegate

abstract class DoxRenderingBase(val i18n: DoxI18N, val bibliography: DoxBibKeyRendering) {

  implicit class StringUtils(base: String) {
    def fulltrim = {
      base.trim()
    }
  }
  
  def refEquation(reference: DoxReferenceEquation) = {
    prefixReference(i18n.equation, reference)
  }
  def refEquationP(reference: DoxReferenceEquation) = {
    prefixReferenceP(i18n.equation, reference)
  }

  def refFigure(reference: DoxReferenceFigure) = {
    prefixReference(i18n.figure, reference)
  }
  def refFigureP(reference: DoxReferenceFigure) = {
    prefixReferenceP(i18n.figure, reference)
  }

  def refTable(reference: DoxReferenceTable) = {
    prefixReference(i18n.table, reference)
  }
  def refTableP(reference: DoxReferenceTable) = {
    prefixReferenceP(i18n.table, reference)
  }

  protected def prefixReference(prefix: String, reference: DoxReferenceLike) = {
    nonBreakingSpace.textNoSpace(prefix).nonBreakingSpace.ref(reference)
  }
  protected def prefixReferenceP(prefix: String, reference: DoxReferenceLike) = {
    nonBreakingSpace.`(`.textNoSpace(prefix).nonBreakingSpace.ref(reference).`)`
  }

  def list(callback: DoxBuilderList => DoxBuilderList): this.type = {
    val result = callback(DoxBuilderList(Seq.empty))
    list(result.args.map(_.in))
    this
  }

  def citet(key: DoxBibKey): this.type = {
    bibliography.append(key)
    nonBreakingSpace
    citet(key.name)
    this
  }
  def citep(key: DoxBibKey): this.type = {
    bibliography.append(key)
    nonBreakingSpace
    citep(key.name)
    this
  }
  def cite(key: DoxBibKey): this.type = {
    bibliography.append(key)
    nonBreakingSpace
    cite(key.name)
    this
  }

  def `(` = {
    this.textNoSpace("(")
  }

  def `)` = {
    this.textNoSpace(")")
  }

  def dot = {
    this.textNoSpace(".")
  }
  
  def break = {
    this.text("\n\n")
  }
  

  def textNoSpace(in: String): this.type = {
    this.text(in.fulltrim)
  }

  def plainNoSpace(in: String): this.type = {
    this.plain(in.fulltrim)
  }

  def label(reference: DoxReferenceLike): this.type
  def chapter(name: String): this.type
  def section(name: String): this.type
  def subsection(name: String): this.type
  def subsubsection(name: String): this.type

  def nonBreakingSpace: this.type

  def text(in: String): this.type
  def textItalic(in: String): this.type

  def plain(in: String): this.type

  def ref(reference: DoxReferenceLike): this.type
  def table(in: DoxTable): this.type
  def clearpage(): this.type

  def svg(callback: DoxBuilderSVG.type => DoxSVGFigureSet): this.type = {
    val data = callback(DoxBuilderSVG)
    this.svg(data)
    this
  }

  protected def svg(imageSetSequence: Seq[DoxSVGFigureSet]): this.type = {
    for (sequence <- imageSetSequence) {
      this.svg(sequence)
    }
    this
  }

  protected def citet(key: String): Unit
  protected def citep(key: String): Unit
  protected def cite(key: String): Unit
  protected def svg(imageSet: DoxSVGFigureSet): Unit
  protected def list(itemSeq: Seq[String]): Unit

}