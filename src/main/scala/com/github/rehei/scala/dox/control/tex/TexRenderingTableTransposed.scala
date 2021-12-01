package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.text.TextAST

class TexRenderingTableTransposed(baseAST: TexAST, toprule: Boolean, model: DoxTable[_], reference: String) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  case class TableContent(contentHeadOffset: TexCommandInline, contentHead: TextAST, contentData: Seq[TextAST])

  protected object ColumnType {
    private val baseString = """\let\newline\\\arraybackslash\hspace{0pt}}m"""
    def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
    def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
    def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)

    private def sizeString(size: Double) = "{" + size + "cm}"
  }
  protected val columnSizeDefault = 3.0
  protected val columnSizeCategory = 4.0
  protected val dataColumnAmount = model.transposed.list().map(_.data.length).max
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  import tmpMarkup._

  def createTableString() = {
    create()
    tmpAST.build()
  }

  protected def create() {
    $ { _ tabular$ & { (columnConfigTotalSize()) } { columnConfigEachColumnSize() } } {
      if (toprule) {
        \ toprule;
      }
      appendTitle()
      appendTable()
      \ bottomrule;
    }
  }

  protected def columnConfigTotalSize() = {
    val tabColSeps = (1 + dataColumnAmount) * 2
    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + (columnSizeCategory + dataColumnAmount * columnSizeDefault) + "cm"
  }
  protected def columnConfigEachColumnSize() = {
    ColumnType.l(columnSizeCategory) ++ (1 to dataColumnAmount).map(_ => ColumnType.r(columnSizeDefault)).mkString
  }

  protected def appendTitle() = {
    if (!Text2TEX.generate(model.title).isEmpty()) {
      \ plain { (\\ multicolumn & { dataColumnAmount + 1 } { "c" } { Text2TEX.generate(model.title) }).generate() }
      \ plain { "\\\\" + "\n" }
      \ midrule
    }
  }

  protected def appendTable() {
    val rows = content()
    for (row <- rows) {
      \ plain { row.contentHeadOffset.generate() }
      \ plain { Text2TEX.generate(row.contentHead) + " & " }
      \ plain { row.contentData.map(data => Text2TEX.generate(data)).mkString(" & ") + "\\\\" + "\n" }
    }
  }

  protected def content() = {
    for (row <- model.transposed.list()) yield {
      TableContent(\\ hspace { (row.columnDepth * 5) + "mm" }, row.head, row.data)
    }
  }

}