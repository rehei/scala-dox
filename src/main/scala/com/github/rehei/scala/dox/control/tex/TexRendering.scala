package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxLikeSVG
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.control.DoxHandleSVG
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering
import com.github.rehei.scala.dox.i18n.DoxI18N
import com.github.rehei.scala.dox.model.DoxReferenceLike
import com.github.rehei.scala.dox.model.DoxReferenceEquation

class TexRendering(
  baseAST:        TexAST,
  indexKeyConfig: DoxTableKeyConfig,
  svgHandle:      DoxHandleSVG,
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

  def chapter(name: String) = {
    \ chapter & { escape(name) }
    this
  }

  def plain(input: String) = {
    \ plain (input)
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

  def textItalic(in: String) = {
    \ textit { escape(in) }
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

  def table(model: DoxTable) = {
    model.withIndex(Some(indexKeyConfig))
    new TexRenderingTable(baseAST, model).create()
    this
  }

  override def svg(svgSet: DoxSVGFigureSet) {
    \ FloatBarrier;
    $ { _ figure & { ###(POSITIONING_FIGURE) } } {
      \ centering;
      for (image <- svgSet.images) {
        appendPDF(image)
      }
      \ caption & { escape(svgSet.config.caption) }
    }
    \ FloatBarrier;
  }

  protected def appendPDF(image: DoxLikeSVG) {
    val filename = svgHandle.serialize(image).toString()

    \ includegraphics & { filename }
  }

  def eqnarray(label: DoxReferenceEquation) = new {
    def expression(expression: String) {
      $ { _.eqnarray } {
        \ plain { expression }
        \ label { label.referenceID }
      }
    }
  }

  protected def citet(key: String) {
    \ citet { key }
  }

  protected def citep(key: String) {
    \ citep { key }
  }

  protected def cite(key: String) {
    \ cite { key }
  }

  def list(itemSeq: Seq[String]) {
    $ { _ itemize } {
      for (item <- itemSeq) {
        \ item & { escape(item) }
      }
    }
  }

}