package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

class TexRenderingTableMulti(baseAST: TexAST, modelMulti: DoxTableSequence, titleOption: Option[TextAST], transposed: Boolean) {

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
    \ plain { modelMulti.models.map(model => "{" + getTable(model) + "}" + verticalSpace).mkString }
  }

  protected def appendTitle() = {
    titleOption.map(
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
      new TexRenderingTableTransposed(baseAST, model, false).createTableString()
    } else {
      new TexRenderingTable(baseAST, model, false).createTableString()
    }
  }

  protected def columnConfigTotalSize() = {
    val columnSizes = modelMulti.columnsMaxWidths(COLUMN_SIZE_DEFAULT)
    val tabColSeps = modelMulti.columnsMaxAmount * 2 + 2

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + columnSizes.sum + "cm"
  }
}