package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

class TexRenderingTableMulti(baseAST: TexAST, modelMulti: DoxTableSequence, titleOption: Option[TextAST], transposed: Boolean) {

  protected val verticalSpace = "\n\\vspace*{0.5cm}" + "\n"
  protected val COLUMN_SIZE_DEFAULT = 2.0
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  protected object ColumnType {
    private val baseString = """\arraybackslash}m{"""
    def c(size: String) = """>{\centering""" + baseString + size + "}"
  }

  def createTableString() = {
    createTitleTable()
    println(tmpAST.build())
    tmpAST.build()
  }

  protected def createTitleTable() {
    $ { _ tabular$ & { (columnConfigTotalSize()) } { "l" } } {
      appendTitle()
      createTables()
      appendBottom()
    }
  }

  def createTables() = {
    modelMulti.models.map(model => {
      \ plain { "{" + getTable(model) + "}" }
      endRowEntry()
    })
  }

  protected def appendTitle() = {
    titleOption.map(
      text => {
        \ plain { "{" }
        ($ { _ tabular$ & { (columnConfigTotalSize()) } { ColumnType.c(columnConfigTotalSize) } } {
          \ toprule;
          \ plain { Text2TEX.generate(text) + "\\\\" }
          \ midrule;
        })
        \ plain { "}" }
        endRowEntry()
      }).getOrElse(None)
  }

  protected def appendBottom() = {
    titleOption.map(
      text => {
        \ plain { "{" }
        $ { _ tabular$ & { (columnConfigTotalSize()) } { ColumnType.c(columnConfigTotalSize) } } {
          \ bottomrule;
        }
        \ plain { "}" }
        endRowEntry()
      }).getOrElse(None)
  }

  protected def getTable(model: DoxTable[_]) = {
    if (transposed) {
      new TexRenderingTableTransposed(baseAST, model, false).createTableString()
    } else {
      new TexRenderingTable(baseAST, model, false).createTableString()
    }
  }

  protected def endRowEntry() = {
    \ plain { verticalSpace + "\\\\ \n" }
  }
  protected def columnConfigTotalSize() = {
    val columnSizes = modelMulti.columnsMaxWidths(COLUMN_SIZE_DEFAULT)
    val tabColSeps = modelMulti.columnsMaxAmount * 2

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + columnSizes.sum + "cm"
  }
}