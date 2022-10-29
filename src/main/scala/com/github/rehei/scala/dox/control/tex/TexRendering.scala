package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.control.DoxHandleEquation
import com.github.rehei.scala.dox.control.DoxHandleSvgTex
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.model.DoxViewModelEquation
import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.model.DoxViewModelTable
import com.github.rehei.scala.dox.model.DoxViewModelTableSequence
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentImage
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.DoxInput

class TexRendering(
  baseAST:        TexAST,
  floating:       Boolean,
  i18n:           DoxI18N,
  bibHandle:      DoxBibKeyRendering,
  tableHandle:    DoxHandleTable,
  equationHandle: DoxHandleEquation,
  svgTexHandle:   DoxHandleSvgTex) extends DoxRenderingBase(i18n, bibHandle) {

  protected val POSITIONING_FIGURE = "H"
  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

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

  protected def internalEquation(equation: DoxViewModelEquation) = {
    val input = equationHandle.handle(equation)

    $ { _ mdframed } {
      $ { _ figure & { ###("H") } } {
        \ input { input.target }
        \ caption & { escape(input.caption) }
      }
    }

  }

  protected def internalTable(table: DoxViewModelTableSequence) {

    val input = tableHandle.handle(table)

    usingFloatBarrier {
      $ { _ table$ & { ###("H") } } {
        tableContent(input)
        \ centering;
        \ caption & { escape(input.caption) }
        \ input { input.target }
      }
    }

  }

  protected def internalTable(table: DoxViewModelTable[_]) {

    val input = tableHandle.handle(table)

    usingFloatBarrier {
      $ { _.using.table(table.model.config.fullpage).apply(###(table.model.config.position)) } {
        tableContent(input)
      }
    }

  }

  protected def tableContent(table: DoxInput) = {

    \ centering;
    \ caption & { escape(table.caption) }
    \ input { table.target }
  }

  protected def internalSvg(svg: DoxViewModelSvg) {

    val input = svgTexHandle.handle(svg)

    usingFloatBarrier {
      $ { _ figure & { ###("H") } } {
        \ input { input.target }
        \ caption & { escape(input.caption) }
      }
    }

  }

  protected def usingFloatBarrier(callback: => Unit) = {
    if (!floating) {
      \ FloatBarrier;
    }

    callback

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

}