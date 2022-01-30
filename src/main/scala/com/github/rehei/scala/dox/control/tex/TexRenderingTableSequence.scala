package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

class TexRenderingTableSequence(baseAST: TexAST, modelSequence: DoxTableSequence, title: TextAST) {
  case class TableConfig(categoryWidth: Double, dataWidth: Double, hasMidrule: Boolean)

  protected val COLUMN_SIZE_DEFAULT = 2.0
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
      appendBottom()
    }
  }

  protected def createTables() = {
    for (model <- modelSequence.sequence) {
      \ plain { "{" + getTable(model) + "}" }
      endRowEntry()
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
    endRowEntry()
  }

  protected def appendBottom() = {
    \ plain { "{" }
    $ { _ tabular$ & { (totalSizeTex()) } { columnTex() } } {
      \ bottomrule;
    }
    \ plain { "}" }
    endRowEntry()
  }

  protected def getTable(model: DoxTable[_]) = {
    new TexRenderingTable(baseAST, model.transform(), true).createTableString()
  }

  protected def endRowEntry() = {
    \ plain { "\n\\vspace*{0.5cm}" + "\n" + "\\\\ \n" }
  }

  protected def columnTex() = {
    ">{\\centering\\arraybackslash}m{" + totalSizeTex() + "}"
  }

  protected def totalSizeTex() = {
    val width = modelSequence.totalWidth(COLUMN_SIZE_DEFAULT)
    val tabColSeps = modelSequence.tabcolSeps()

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + width + "cm"
  }

}