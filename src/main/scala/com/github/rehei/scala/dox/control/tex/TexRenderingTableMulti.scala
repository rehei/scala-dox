package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableMulti
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX

class TexRenderingTableMulti(baseAST: TexAST, modelMulti: DoxTableMulti, title: Option[TextAST], transposed: Boolean) {

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
    title.map(
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
    transposed match {
      case false => { new TexRenderingTable(baseAST, model, true).createTableString() }
      case true  => { new TexRenderingTableTransposed(baseAST, model, true).createTableString() }
    }
  }

  protected def columnConfigTotalSize() = {
    val columnSizes = modelMulti.columnsMaxWidths
    val tabColSeps = modelMulti.columnsMaxAmount * 2 + 2

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + columnSizes.sum + "cm"
  }
}