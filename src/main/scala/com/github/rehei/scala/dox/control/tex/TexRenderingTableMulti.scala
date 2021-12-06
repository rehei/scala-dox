package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableMulti
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

class TexRenderingTableMulti(baseAST: TexAST, modelMulti: DoxTableMulti, transposed: Boolean) {

  protected val verticalSpace = "\n\\vspace*{1cm}\\\\"
  protected val COLUMN_SIZE_DEFAULT = 2.0
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  def createTableString() = {
    create()
    tmpAST.build()
  }

  protected def create() {

    $ { _ tabular$ & { (columnConfigTotalSize()) } { "c" } } {

      \ toprule;
      appendTitle()
      createTables()
      \ bottomrule;
    }
  }

  def createTables() = {
    \ plain { modelMulti.content.map(model => "{" + getTable(model) + "}" + verticalSpace).mkString }
  }

  protected def appendTitle() = {
    modelMulti.title.map(
      text => {
        \ plain { Text2TEX.generate(text) }
        topruleEndRow()
      }).getOrElse(None)
  }

  protected def topruleEndRow() = {
    \ plain { "\\toprule" } + endRow().generate()
  }

  protected def endRow() = {
    \ plain { "\\\\" + "\n" }
  }

  protected def getTable(model: DoxTable[_]) = {
    if (transposed) {
      new TexRenderingTableTransposed(baseAST, model, None, true).createTableString()
    } else {
      new TexRenderingTable(baseAST, model, None, true).createTableString()
    }
  }

  protected def columnConfigTotalSize() = {
    val columnSizes = modelMulti.columnsMaxWidths
    val tabColSeps = modelMulti.columnsMaxAmount * 2 + 2

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + columnSizes.sum + "cm"
  }
}