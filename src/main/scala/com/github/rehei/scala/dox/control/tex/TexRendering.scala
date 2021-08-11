package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.control.DoxHandleSvg
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxReferenceEquation
import com.github.rehei.scala.dox.model.DoxReferenceLike
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.table.DoxTable

class TexRendering(
  baseAST:        TexAST,
  floating:       Boolean,
  indexKeyConfig: DoxTableKeyConfig,
  svgHandle:      DoxHandleSvg,
  i18n:           DoxI18N,
  bibHandle:      DoxBibKeyRendering) extends DoxRenderingBase(i18n, bibHandle) {

  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  protected val POSITIONING_FIGURE = "H"

  def label(reference: DoxReferenceLike) = {
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

  def ref(reference: DoxReferenceLike) = {
    \ ref { reference.referenceID }
    this
  }

  def table(reference: DoxReferenceTable, model: DoxTable[_]) = {
    new TexRenderingTable(baseAST, floating, model, reference).create()
    this
  }
  
  def eqnarray(label: DoxReferenceEquation, expression: String) = {
    $ { _.eqnarray } {
      \ plain { expression }
      \ label { label.referenceID }
    }
    this
  }

  protected def internalSvg(svg: DoxSvgFigure) {
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ figure & { ###(POSITIONING_FIGURE) } } {
      \ centering;
      appendTransformableSVG(svg)
      \ caption & { escape(svg.config.caption) }
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

}