package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.control.DoxHandleEquation
import com.github.rehei.scala.dox.control.DoxHandleSvg
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
import com.github.rehei.scala.dox.model.reference.DoxReferenceText
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxFileTable
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.control.DoxHandleTex
import com.github.rehei.scala.dox.model.DoxFileTex

class TexRendering(
  baseAST:        TexAST,
  floating:       Boolean,
  svgHandle:      DoxHandleSvg,
  i18n:           DoxI18N,
  bibHandle:      DoxBibKeyRendering,
  tableHandle:    DoxHandleTable,
  equationHandle: DoxHandleEquation,
  texHandle:      DoxHandleTex) extends DoxRenderingBase(i18n, bibHandle) {

  case class TexRenderingSVG(svg: DoxSvgFigure) {

    case class HasTitle() extends SvgMode {
      protected val tmpAST = new TexAST
      protected val tmpMarkup = new TexMarkupFactory(tmpAST)
      import tmpMarkup._

      protected val factor = 0.9
      protected val tableTotalSize = "\\dimexpr(\\tabcolsep*2)+" + factor + "\\textwidth"
      protected val columnTotalSize = " >{\\raggedright\\arraybackslash}p{" + factor + "\\textwidth}"

      def appendSvgToAST() = {
        \ vspace { "4pt" };
        \ centering;
        appendTitle()
        \ includegraphics { createFileAndGetPath() }
        appendBottom()
      }

      def buildAST() = {
        tmpAST.build()
      }

      protected def appendTitle() {
        $ { _ tabular$ & { (tableTotalSize) } { columnTotalSize } } {
          \ toprule;
          \ plain { svg.titleOption.getOrElse(throw new IllegalArgumentException("Title missing")) + "\\\\" }
          \ midrule;
        }
      }

      protected def appendBottom() {
        $ { _ tabular$ & { (tableTotalSize) } { columnTotalSize } } {
          \ bottomrule;
        }
      }
    }

    case class HasNoTitle() extends SvgMode {
      protected val tmpAST = new TexAST
      protected val tmpMarkup = new TexMarkupFactory(tmpAST)
      import tmpMarkup._

      def appendSvgToAST() = {
        \ vspace { "4pt" };
        \ centering;
        \ includegraphics { createFileAndGetPath() }
      }

      def buildAST() = {
        tmpAST.build()
      }
    }

    abstract class SvgMode() {

      def appendSvgToAST(): Unit
      def buildAST(): String
      
      protected def createFileAndGetPath() = {
        assume(svg.titleOption.map(m => !(m.contains("\n"))).getOrElse(true))
        svgHandle.serialize(svg).toString()
      }
    }

    protected val svgMode = {
      if (svg.titleOption.isDefined) {
        HasTitle()
      } else {
        HasNoTitle()
      }
    }

    def generate() = {
      svgMode.appendSvgToAST()
      texHandle.serialize(DoxFileTex(svgMode.buildAST(), svg.label)).toString()
    }
  }

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
    val filename = equationHandle.serialize(DoxFileEquation(texEquations, equation.label))
    $ { _ mdframed } {
      $ { _ figure & { ###("H") } } {
        \ input { filename }
        \ caption & { escape(fileCaption(equation.label)) }
      }
    }
  }

  protected def internalTable(table: DoxTableViewModelSequence) {
    val texTable = new TexRenderingTableSequence(baseAST, table.models, table.title).createTableString()
    val filename = tableHandle.serialize(DoxFileTable(texTable, table.label))
    if (!floating) {
      \ FloatBarrier;
    }

    $ { _ table & { ###("H") } } {

      \ centering;
      \ input { filename }
      \ caption & { escape(fileCaption(table.label)) }
    }

    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def internalTable(table: DoxTableViewModel[_]) {
    val texTable = new TexRenderingTable(baseAST, table.model.transform(), false).createTableString()
    val filename = tableHandle.serialize(DoxFileTable(texTable, table.label))
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      \ input { filename }
      \ caption & { escape(fileCaption(table.label)) }
    }
    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def internalSvg(svg: DoxSvgFigure) {
    // INFO: Label MUST NOT contain backslash -> escaping introduces \
    if (!floating) {
      \ FloatBarrier;
    }

    $ { _ figure & { ###("H") } } {
      \ input { TexRenderingSVG(svg).generate() }
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

  protected def appendTransformableSVG(figure: DoxSvgFigure) {
    val filename = svgHandle.serialize(figure).toString()
    \ includegraphics & { filename }
  }

  protected def fileCaption(label: Option[DoxReferenceBase]) = {
    label.map(m => m.name + " | " + m.hashID).getOrElse("dummylabel")
  }

  protected def fileLabel(label: Option[DoxReferenceBase]) = {
    label.map(_.hashID).getOrElse("dummylabel")
  }
}