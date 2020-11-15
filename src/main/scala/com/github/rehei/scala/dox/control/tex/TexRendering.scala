package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxLikeSVG
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.DoxReference
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.control.DoxHandleSVG
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyRendering

class TexRendering(baseAST: TexAST, indexKeyConfig: DoxTableKeyConfig, svgHandle: DoxHandleSVG, bibHandle: DoxBibKeyRendering) extends DoxRenderingBase(bibHandle) {

  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  protected val POSITIONING_FIGURE = "H"

  def label(reference: DoxReference) {
    \ label { reference.referenceID }
  }

  def clearpage() {
    \ clearpage;
  }

  def chapter(name: String) {
    \ chapter & { escape(name) }
  }

  def section(name: String) {
    \ section & { escape(name) }
  }

  def subsection(name: String) {
    \ subsection & { escape(name) }
  }

  def subsubsection(name: String) {
    \ subsubsection & { escape(name) }
  }

  def text(in: String) {
    \ plain { " " + escape(in) + " " }
  }

  def ref(reference: DoxReference) = {
    \ ref { reference.referenceID }
  }

  protected def citet(key: String) = {
    \ citet { key }
  }

  protected def citep(key: String) = {
    \ citep { key }
  }

  protected def cite(key: String) = {
    \ cite { key }
  }

  def table(model: DoxTable) {
    model.withIndex(Some(indexKeyConfig))

    new TexRenderingTable(baseAST, model).create()
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
    val filename = svgHandle.serialize(image).toAbsolutePath().toString()

    \ includegraphics & { ###("scale=0.5") } { filename }
  }

  def list(itemSeq: Seq[String]) {
    $ { _ itemize } {
      for (item <- itemSeq) {
        \ item & { escape(item) }
      }
    }
  }

}