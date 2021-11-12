package com.github.rehei.scala.dox.control

import scala.collection.Seq

import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxLabelTable
import com.github.rehei.scala.dox.model.DoxReferenceEquation
import com.github.rehei.scala.dox.model.DoxReferenceFigure
import com.github.rehei.scala.dox.model.DoxReferenceLike
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.table.DoxTable

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
    nonBreakingSpace.text(prefix).nonBreakingSpace.ref(reference)
  }
  protected def prefixReferenceP(prefix: String, reference: DoxReferenceLike) = {
    nonBreakingSpace.`(`.text(prefix).nonBreakingSpace.ref(reference).`)`
  }

  def list(callback: DoxBuilderList => DoxBuilderList): this.type = {
    val result = callback(DoxBuilderList(Seq.empty))
    internalList(result.args.map(_.in))
    this
  }

  def citeT(key: DoxBibKey): this.type = {
    bibliography.append(key)
    nonBreakingSpace
    internalCiteT(key.name)
    this
  }
  def citeP(key: DoxBibKey): this.type = {
    bibliography.append(key)
    nonBreakingSpace
    internalCiteP(key.name)
    this
  }
  def cite(key: DoxBibKey): this.type = {
    bibliography.append(key)
    nonBreakingSpace
    internalCite(key.name)
    this
  }

  def `(` = {
    this.text("(")
  }

  def `)` = {
    this.text(")")
  }

  def dot = {
    this.text(".")
  }

  def space = {
    this.text(" ")
  }

  def dash = {
    this.nonBreakingSpace.text("--").space
  }

  def cf = {
    this.text("cf.")
  }

  def break = {
    this.plain("\n\n")
  }

  def breakline = {
    this
  }

  def text(in: String): this.type = {
    internalText(in)
    this
  }

  def textItalic(in: String): this.type

  def textRed(in: String): this.type

  def plain(in: String): this.type = {
    internalPlain(in)
    this
  }

  def label(reference: DoxReferenceLike): this.type
  def chapter(name: String): this.type
  def section(name: String): this.type
  def subsection(name: String): this.type
  def subsubsection(name: String): this.type

  def nonBreakingSpace: this.type

  def ref(reference: DoxReferenceLike): this.type
  def table(callback: DoxBuilderTable.type => DoxLabelTable[_]): this.type = {
    val data = callback(DoxBuilderTable)
    internalTable(data)
    this
  }

  def eqnarray(label: DoxReferenceEquation, expression: String): this.type

  def clearpage(): this.type

  def svg(callback: DoxBuilderSvg.type => DoxSvgFigure): this.type = {
    val data = callback(DoxBuilderSvg)
    internalSvg(data)
    this
  }

  protected def svg(imageSetSequence: Seq[DoxSvgFigure]): this.type = {
    for (sequence <- imageSetSequence) {
      internalSvg(sequence)
    }
    this
  }

  protected def internalText(in: String): Unit
  protected def internalPlain(in: String): Unit

  protected def internalCiteT(key: String): Unit
  protected def internalCiteP(key: String): Unit
  protected def internalCite(key: String): Unit
  protected def internalSvg(imageSet: DoxSvgFigure): Unit
  protected def internalTable(table: DoxLabelTable[_]): Unit
  protected def internalList(itemSeq: Seq[String]): Unit

}