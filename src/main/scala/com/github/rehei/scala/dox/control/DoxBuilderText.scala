package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.model.DoxDelegate
import com.github.rehei.scala.dox.model.DoxReference

case class DoxBuilderText(base: DoxRenderingBase, args: Seq[DoxDelegate]) {

  def `(` = {
    copyAppend(DoxDelegate(() => base.textNoSpace("(")))
  }

  def `)` = {
    copyAppend(DoxDelegate(() => base.textNoSpace(")")))
  }

  def dot = {
    copyAppend(DoxDelegate(() => base.textNoSpace(".")))
  }

  def nonBreakingSpace = {
    copyAppend(DoxDelegate(() => base.nonBreakingSpace))
  }

  def text(in: String) = {
    copyAppend(DoxDelegate(() => base.textNoSpace(" " + in))) // yes, that is right.
  }

  def textItalic(in: String) = {
    copyAppend(DoxDelegate(() => base.textItalic(" " + in)))
  }

  def textNoSpace(in: String) = {
    copyAppend(DoxDelegate(() => base.textNoSpace(in)))
  }

  def plain(in: String) = {
    copyAppend(DoxDelegate(() => base.plain(in)))
  }

  def ref(reference: DoxReference) = {
    copyAppend(DoxDelegate(() => base.ref(reference)))
  }
  def citet(reference: DoxBibKey) = {
    copyAppend(DoxDelegate(() => base.citet(reference)))
  }
  def citep(reference: DoxBibKey) = {
    copyAppend(DoxDelegate(() => base.citep(reference)))
  }
  def cite(reference: DoxBibKey) = {
    copyAppend(DoxDelegate(() => base.cite(reference)))
  }

  def refEquation(reference: DoxReference) = {
    prefixReference(base.i18n.equation, reference)
  }
  def refEquationP(reference: DoxReference) = {
    prefixReferenceP(base.i18n.equation, reference)
  }

  def refFigure(reference: DoxReference) = {
    prefixReference(base.i18n.figure, reference)
  }
  def refFigureP(reference: DoxReference) = {
    prefixReferenceP(base.i18n.figure, reference)
  }

  def refTable(reference: DoxReference) = {
    prefixReference(base.i18n.table, reference)
  }
  def refTableP(reference: DoxReference) = {
    prefixReferenceP(base.i18n.table, reference)
  }

  protected def prefixReference(prefix: String, reference: DoxReference) = {
    copyAppend(DoxDelegate(() => base.text(_.nonBreakingSpace.textNoSpace(prefix).nonBreakingSpace.ref(reference))))
  }
  protected def prefixReferenceP(prefix: String, reference: DoxReference) = {
    copyAppend(DoxDelegate(() => base.text(_.nonBreakingSpace.`(`.textNoSpace(prefix).nonBreakingSpace.ref(reference).`)`)))
  }

  def flush() {
    for (arg <- args) {
      arg.callback()
    }
  }
  protected def copyAppend(value: DoxDelegate) = {
    this.copy(args = args :+ value)
  }

}