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

class TexRenderingTable2(baseAST: TexAST, floating: Boolean, model: DoxTable[_], reference: String) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  case class TableContent(contentHeadOffset: TexCommandInline, contentHead: TextAST, contentData: Seq[TextAST])

  protected object ColumnType {
    private val baseString = """\let\newline\\\arraybackslash\hspace{0pt}}m"""
    def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
    def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
    def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)

    private def sizeString(size: Double) = "{" + size + "cm}"
  }
  protected val columnSizeDefault = 1.0
  protected val columnSizeCategory = 3.0
  protected val dataColumnAmount = model.transposed.list().map(_.data.length).max
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  import tmpMarkup._

  def createTableString() = {
    create()
    println(tmpAST.build())
    tmpAST.build()
  }

  protected def create() {

    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      $ { _ tabular$ & { (columnConfigTotalSize()) } { columnConfigEachColumnSize() } } {
        \ toprule;
        appendTitle()
        \ midrule;
        appendTable()
        \ bottomrule;
      }
      \ caption & { model.caption }
      \ label { reference }
    }
    if (!floating) {
      \ FloatBarrier;
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
    \ plain { (\\ multicolumn & { dataColumnAmount + 1 } { "c" } { Text2TEX.generate(model.transposed.title) }).generate() }
    \ plain { "\\\\" + "\n" }
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