package com.github.rehei.scala.dox.control

import scala.collection.Seq

import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxViewModelTable
import com.github.rehei.scala.dox.model.DoxViewModelTableSequence
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.DoxViewModelEquation

abstract class DoxRenderingBase(val i18n: DoxI18N, val bibliography: DoxBibKeyRendering) {

  implicit class StringUtils(base: String) {
    def fulltrim = {
      base.trim()
    }
  }

  def refEquation(reference: DoxReferencePersistentEquation) = {
    prefixReference(i18n.equation, reference)
  }

  def refEquationP(reference: DoxReferencePersistentEquation) = {
    prefixReferenceP(i18n.equation, reference)
  }

  def refFigure(reference: DoxReferencePersistentTable) = {
    prefixReference(i18n.figure, reference)
  }

  def refFigureP(reference: DoxReferencePersistentTable) = {
    prefixReferenceP(i18n.figure, reference)
  }

  def refTable(reference: DoxReferencePersistentTable) = {
    prefixReference(i18n.table, reference)
  }

  def refTableP(reference: DoxReferencePersistentTable) = {
    prefixReferenceP(i18n.table, reference)
  }

  protected def prefixReference(prefix: String, reference: DoxReferenceBase) = {
    nonBreakingSpace.text(prefix).nonBreakingSpace.ref(reference)
  }
  protected def prefixReferenceP(prefix: String, reference: DoxReferenceBase) = {
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

  def text(in: TextAST): this.type

  def text(in: String): this.type

  def textItalic(in: String): this.type

  def textRed(in: String): this.type

  def plain(in: String): this.type = {
    internalPlain(in)
    this
  }

  def pagename(name: String): this.type
  def label(reference: DoxReferenceText): this.type
  def chapter(name: String): this.type
  def section(name: String): this.type
  def subsection(name: String): this.type
  def subsubsection(name: String): this.type

  def nonBreakingSpace: this.type

  def ref(reference: DoxReferenceBase): this.type

  def tablePlain(content: String): this.type = {
    internalTablePlain(content)
    this
  }

  def table(callback: DoxBuilderTable.type => DoxViewModelTable[_]): this.type = {
    val data = callback(DoxBuilderTable)
    internalTable(data)
    this
  }

  def tableSequence(callback: DoxBuilderTableSequence.type => DoxViewModelTableSequence): this.type = {
    val data = callback(DoxBuilderTableSequence)
    internalTable(data)
    this
  }

  def tableSequenceCondensed(callback: DoxBuilderTableSequenceCondensed.type => DoxViewModelTableSequence): this.type = {
    val data = callback(DoxBuilderTableSequenceCondensed)
    internalTable(data)
    this
  }

  def equation(callback: DoxBuilderEquation.type => DoxViewModelEquation) = {
    val data = callback(DoxBuilderEquation)
    internalEquation(data)
    this
  }

  def clearpage(): this.type

  def svg(callback: DoxBuilderSvg.type => DoxViewModelSvg): this.type = {
    val data = callback(DoxBuilderSvg)
    internalSvg(data)
    this
  }

  protected def internalPlain(in: String): Unit
  protected def internalCiteT(key: String): Unit
  protected def internalCiteP(key: String): Unit
  protected def internalCite(key: String): Unit
  protected def internalSvg(imageSet: DoxViewModelSvg): Unit
  protected def internalTablePlain(content: String): Unit
  protected def internalTable(table: DoxViewModelTableSequence): Unit
  protected def internalTable(table: DoxViewModelTable[_]): Unit
  protected def internalEquation(table: DoxViewModelEquation): Unit
  protected def internalList(itemSeq: Seq[String]): Unit

}