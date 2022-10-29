package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.control.DoxHandleEquation
import com.github.rehei.scala.dox.control.DoxHandleSvgTex
import com.github.rehei.scala.dox.control.DoxHandleTable
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.model.DoxFileEquation
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.DoxTableViewModelSequence
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentImage
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX

class TexRendering(
  baseAST:        TexAST,
  floating:       Boolean,
  i18n:           DoxI18N,
  bibHandle:      DoxBibKeyRendering,
  tableHandle:    DoxHandleTable,
  equationHandle: DoxHandleEquation,
  svgTexHandle:   DoxHandleSvgTex,
  style:          TexRenderingStyle) extends DoxRenderingBase(i18n, bibHandle) {

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

  protected def internalEquation(equation: DoxEquation) = {
    val texEquations = new TexRenderingEquation(baseAST, equation).createEquationString()
    val file = DoxFileEquation(texEquations, equation.label)
    val filename = equationHandle.serialize(file)
    $ { _ mdframed } {
      $ { _ figure & { ###("H") } } {
        \ input { filename }
        \ caption & { escape(file.fileCaption) }
      }
    }
  }

  protected def internalTable(table: DoxTableViewModelSequence) {

    val data = table.serialize(tableHandle, baseAST, style)

    if (!floating) {
      \ FloatBarrier;
    }

    $ { _ table$ & { ###("H") } } {
      \ centering;
      \ caption & { escape(data.caption) }
      \ input { data.filename }
    }

    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def internalTable(table: DoxTableViewModel[_]) {
    if (!floating) {
      \ FloatBarrier;
    }

    $ { _.using.table(table.model.config.fullpage).apply(###(table.model.config.position)) } {
      tableContent(table)
    }

    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def tableContent(table: DoxTableViewModel[_]) = {

    val data = table.serialize(tableHandle, baseAST, style)

    \ centering;
    \ caption & { escape(data.caption) }
    \ input { data.filename }
  }

  protected def internalSvg(svg: DoxSvgFigure) {

    def fileCaption(label: Option[DoxReferencePersistentImage]) = {
      label.map(m => m.name + " | " + m.hashID).getOrElse("dummylabel")
    }

    def fileLabel(label: Option[DoxReferencePersistentImage]) = {
      label.map(_.hashID).getOrElse("dummylabel")
    }

    if (!floating) {
      \ FloatBarrier;
    }

    $ { _ figure & { ###("H") } } {
      \ input { svgTexHandle.serialize(svg) }
      \ caption & { escape(fileCaption(svg.label)) }
      \ label { fileLabel(svg.label) }
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

}