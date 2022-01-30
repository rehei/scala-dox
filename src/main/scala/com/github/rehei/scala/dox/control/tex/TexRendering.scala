package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.control.DoxHandleEquation
import com.github.rehei.scala.dox.control.DoxHandleSvg
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.model.DoxEquationFile
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.DoxTableViewModelSequence
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableFile
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX

class TexRendering(
  baseAST:        TexAST,
  floating:       Boolean,
  svgHandle:      DoxHandleSvg,
  i18n:           DoxI18N,
  bibHandle:      DoxBibKeyRendering,
  tableHandle:    DoxHandleTable,
  equationHandle: DoxHandleEquation) extends DoxRenderingBase(i18n, bibHandle) {

  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  protected val POSITIONING_FIGURE = "H"

  def label(reference: DoxReferenceText) = {
    \ label { reference.name }
    this
  }

  def clearpage() = {
    \ clearpage;
    this
  }

  def newpage() = {
    \ newpage;
    this
  }

  def bigskip() = {
    \ bigskip;
    this
  }

  def bigskip(count: Int) = {
    for (i <- scala.Range.inclusive(1, count)) {
      \ bigskip;
    }
    this
  }

  def chapter(name: String) = {
    \ chapter & { escape(name) }
    this
  }

  def section(name: String) = {
    \ section & { escape(name) }
    this
  }

  def section$(name: String) = {
    \ section$ & { escape(name) }
    this
  }

  def subsection(name: String) = {
    \ subsection & { escape(name) }
    this
  }

  def subsubsection(name: String) = {
    \ subsubsection & { escape(name) }
    this
  }

  def text(in: String) = {
    \ plain { escape(in) }
    this
  }

  def text(in: TextAST) = {
    \ plain { Text2TEX.generate(in) }
    this
  }

  def textItalic(in: String) = {
    \ textit { escape(in) }
    this
  }

  def textRed(in: String) = {
    \ textcolor & { "red" } { escape(in) }
    this
  }

  def nonBreakingSpace = {
    \ plain { "~" }
    this
  }

  def ref(reference: DoxReferenceBase) = {
    \ ref { reference.name }
    this
  }

  protected def internalEquation(equation: DoxEquation) = {
    val texEquations = new TexRenderingEquation(baseAST, equation).createEquationString()
    val filename = equationHandle.serialize(DoxEquationFile(texEquations, equation.label))
    $ { _ mdframed } {
      $ { _ figure & { ###("H") } } {
        \ input { filename }
        \ caption & { escape(fileLabel(equation.label)) }
      }
    }
  }

  protected def internalTable(table: DoxTableViewModelSequence) {
    val texTable = new TexRenderingTableSequence(baseAST, table.models, table.title).createTableString()
    val filename = tableHandle.serialize(DoxTableFile(texTable, table.label))
    if (!floating) {
      \ FloatBarrier;
    }

    $ { _ table & { ###("H") } } {

      \ centering;
      \ input { filename }
      \ caption & { escape(fileLabel(table.label)) }
    }

    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def internalTable(table: DoxTableViewModel[_]) {
    val texTable = new TexRenderingTable(baseAST, table.model.transform(), false).createTableString()
    val filename = tableHandle.serialize(DoxTableFile(texTable, table.label))
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      \ input { filename }
      \ caption & { escape(fileLabel(table.label)) }
    }
    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def internalSvg(svg: DoxSvgFigure) {
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ figure & { ###(POSITIONING_FIGURE) } } {
      \ centering;
      appendTransformableSVG(svg)
      \ caption & { escape(fileLabel(svg.label)) }
    }
    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def internalCiteT(key: String) {
    \ citet { key }
  }

  protected def internalCiteP(key: String) {
    \ citep { key }
  }

  protected def internalCite(key: String) {
    \ cite { key }
  }

  protected def internalList(itemSeq: Seq[String]) {
    $ { _ itemize } {
      for (item <- itemSeq) {
        \ item & { escape(item) }
      }
    }
  }

  protected def internalPlain(input: String) {
    \ plain (input)
  }

  protected def appendTransformableSVG(figure: DoxSvgFigure) {
    val filename = svgHandle.serialize(figure).toString()
    \ includegraphics & { filename }
  }

  protected def fileLabel(label: Option[DoxReferenceBase]) = {
    label.map(m => m.name + " | " + m.hashID).getOrElse("dummylabel")
  }
}