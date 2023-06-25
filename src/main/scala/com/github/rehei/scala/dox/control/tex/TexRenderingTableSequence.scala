package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.model.table.content.DoxContent.DoxLegend

class TexRenderingTableSequence(modelSequence: DoxTableSequence, title: TextAST, hintOption: Option[DoxLegend], style: TexRenderingStyle) {
  case class TableConfig(categoryWidth: Double, dataWidth: Double, hasMidrule: Boolean)

  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  def createTableString() = {
    createTableSequence()
    tmpAST.build()
  }

  protected def createTableSequence() {
    $ { _ tabular$ & { (totalSizeTex()) } { "l" } } {
      appendTitle()
      createTables()
      appendTableLegend()
      appendBottom()
    }
  }

  protected def createTables() = {
    for (model <- modelSequence.sequence) {
      \ plain { "{" + getTable(model) + "}" }
      verticalSpacing()
    }
  }

  protected def appendTitle() = {
    \ plain { "{" }
    ($ { _ tabular$ & { (totalSizeTex()) } { columnTex() } } {
      \ toprule;
      \ plain { Text2TEX(false).generate(title) + "\\\\" }
      \ midrule;
    })
    \ plain { "}" }
    verticalSpacing()
  }

  protected def appendBottom() = {
    \ plain { "{" }
    $ { _ tabular$ & { (totalSizeTex()) } { columnTex() } } {
      \ bottomrule;
    }
    \ plain { "}" }
    verticalSpacing()
  }

  protected def getTable(model: DoxTableMatrix) = {
    new TexRenderingTable(model, true, style).createTableString()
  }

  protected def verticalSpacing() = {
    \ plain { "\n\\vspace*{0.5cm}" + "\n" + "\\\\ \n" }
  }

  protected def columnTex() = {
    ">{\\centering\\arraybackslash}m{" + totalSizeTex() + "}"
  }

  protected def totalSizeTex() = {
    val width = modelSequence.totalWidth()
    val tabColSeps = modelSequence.totalSeparatorCount()

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + width + "cm"
  }

  protected def appendTableLegend() {
    for (hint <- hintOption) {
      val legend = new TexRenderingTableLegend("Hinweis", Seq(hint)).createTableString()
      \ plain { legend }
      \ plain { "\\\\" }
    }

  }

}