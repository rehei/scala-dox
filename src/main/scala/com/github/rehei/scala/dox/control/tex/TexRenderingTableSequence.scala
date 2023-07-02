package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.model.table.content.DoxContent.DoxLegend

class TexRenderingTableSequence(modelSequence: DoxTableSequence, titleOption: Option[TextAST], hintOption: Option[DoxLegend], style: TexRenderingStyle) {
  case class TableConfig(categoryWidth: Double, dataWidth: Double, hasMidrule: Boolean)

  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  def createTableString() = {
    createTableSequence()
    tmpAST.build()
  }

  protected def createTableSequence() {
    $ { _ tabular$ & { (totalSizeTex()) } { "@{}l@{}" } } {
      \ toprule;
      for (title <- titleOption) {
        appendTitle(title)
      }
      for (model <- modelSequence.sequence.headOption) {
        \ plain { getTable(model) }
        verticalSpacing()

        for (model <- modelSequence.sequence.tail) {
          \ toprule;
          \ plain { getTable(model) }
          verticalSpacing()
        }
      }
      appendTableLegend()
      \ bottomrule;
    }
  }

  protected def appendTitle(title: TextAST) = {
    $ { _ tabular$ & { (totalSizeTex()) } { ">{\\centering\\arraybackslash}m{" + totalSizeTex() + "}" } } {
      \ plain { Text2TEX(false).generate(title) + "\\\\" }
      \ midrule;
    }
    verticalSpacing()
  }

  protected def getTable(model: DoxTableMatrix) = {
    new TexRenderingTable(model, true, false, titleOption.isDefined, style).createTableString()
  }

  protected def verticalSpacing() = {
    if (titleOption.isDefined) {
      \ plain { "\n\\vspace*{0.5cm}" + "\n" + "\\\\ \n" }
    } else {
      \ plain { "\\\\ \n" }
    }
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