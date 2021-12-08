package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.reference.DoxReferenceEquation
import com.github.rehei.scala.dox.control.DoxHandleSvg
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxTableViewModelSequence
import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable
import com.github.rehei.scala.dox.model.table.DoxTableFile
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.text.TextAST

class TexRendering(
  baseAST:        TexAST,
  floating:       Boolean,
  indexKeyConfig: DoxTableKeyConfig,
  svgHandle:      DoxHandleSvg,
  i18n:           DoxI18N,
  bibHandle:      DoxBibKeyRendering,
  tableHandle:    DoxHandleTable) extends DoxRenderingBase(i18n, bibHandle) {

  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  protected val POSITIONING_FIGURE = "H"

  def label(reference: DoxReferenceText) = {
    \ label { reference.referenceID }
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
    \ ref { reference.referenceID }
    this
  }

  def eqnarray(label: DoxReferenceEquation, expression: String) = {
    $ { _.eqnarray } {
      \ plain { expression }
      \ label { label.referenceID }
    }
    this
  }

  protected def internalTable(table: DoxTableViewModelSequence) {
    val texTable = new TexRenderingTableMulti(baseAST, table.models, table.titleOption, table.transposed).createTableString()
    val filename = tableHandle.serialize(DoxTableFile(texTable, table.label))
    if (!floating) {
      \ FloatBarrier;
    }

    $ { _ table & { ###("H") } } {

      \ centering;
//      $ { _ tabular$ & { "\\textwidth" } { "l" } } {
        \ input { filename }

//      }
      \ caption & { escape(tableLabel(table.label)) }
    }

    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def internalTable(table: DoxTableViewModel[_]) {
    val texTable = getTable(table.model, table.label, table.transposed, false)
    val filename = tableHandle.serialize(DoxTableFile(texTable, table.label))
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      \ input { filename }
      \ caption & { escape(tableLabel(table.label)) }
    }
    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def getTable(model: DoxTable[_], label: Option[DoxReferencePersistentTable], transposed: Boolean, isInnerTable: Boolean) = {
    if (transposed) {
      new TexRenderingTableTransposed(baseAST, model, isInnerTable).createTableString()
    } else {
      new TexRenderingTable(baseAST, model, isInnerTable).createTableString()
    }

  }

  protected def internalSvg(svg: DoxSvgFigure) {
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ figure & { ###(POSITIONING_FIGURE) } } {
      \ centering;
      appendTransformableSVG(svg)
      \ caption & { escape(tableLabel(svg.config.label)) }
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

  protected def internalText(in: String) {
    \ plain { escape(in) }
  }

  protected def internalPlain(input: String) {
    \ plain (input)
  }

  protected def appendTransformableSVG(figure: DoxSvgFigure) {
    val filename = svgHandle.serialize(figure).toString()

    \ includegraphics & { filename }
  }

  protected def tableLabel(label: Option[DoxReferenceBase]) = {
    label.map(_.referenceID).getOrElse("dummylabel")
  }
}