package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigExtended
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.text.TextFactory

class TexRenderingTableTransposed(baseAST: TexAST, model: DoxTable[_], titleOption: Option[TextAST], isInnerTable: Boolean) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  case class TableContent(contentHeadOffset: TexCommandInline, contentHead: TextAST, contentData: Seq[TextAST])
  case class TableConfig(categoryWidth: Double, dataWidth: Double)
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

  protected val columnWidths = tableConfig()
  protected val modelTransposed = model.transposed
  import tmpMarkup._

  def createTableString() = {
    create()
    tmpAST.build()
  }

  protected def create() {
    $ { _ tabular$ & { (columnConfigTotalSize()) } { columnConfigEachColumnSize() } } {
      if (!isInnerTable) {
        \ toprule;
      }
      appendTitle()
      appendTable()
      \ bottomrule;
    }
  }

  protected def columnConfigTotalSize() = {
    val tabColSeps = (1 + dataColumnAmount) * 2
    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + (columnWidths.categoryWidth + dataColumnAmount * columnWidths.dataWidth) + "cm"
  }
  protected def columnConfigEachColumnSize() = {
    ColumnType.l(columnWidths.categoryWidth) ++ (1 to dataColumnAmount).map(col => getTableAlignment()).mkString
  }

  protected def appendTitle() = {
    if (showTitle) {
      \ plain { (\\ multicolumn & { dataColumnAmount + 1 } { "c" } { titleAST() }).generate() }
      \ plain { "\\\\" + "\n" }
      \ midrule
    }
  }

  protected def titleAST() = {
    titleOption.map(
      title => {
        Text2TEX.generate(title)
      }).getOrElse("")
  }
  protected def tableConfig() = {
    TableConfig(model.root.config.width.getOrElse(columnSizeCategory), model.root.config.transposedWidth.getOrElse(columnSizeDefault))
  }
  protected def showTitle() = {
    titleOption.isDefined && !isInnerTable
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
    for (row <- modelTransposed.list()) yield {
      TableContent(\\ hspace { (row.columnDepth * 5) + "mm" }, row.head, row.data)
    }
  }

  protected def getTableAlignment() = {
    model.root.config.base.alignment match {
      case DoxTableAlignment.LEFT   => ColumnType.l(columnWidths.dataWidth)
      case DoxTableAlignment.RIGHT  => ColumnType.r(columnWidths.dataWidth)
      case DoxTableAlignment.CENTER => ColumnType.c(columnWidths.dataWidth)
      case _                        => ColumnType.r(columnWidths.dataWidth)
    }
  }
  //  protected def getTexAlignment(config: DoxTableKeyConfigExtended) = {
  //    val size = config.width.getOrElse(columnSizeDefault)
  //    config.base.alignment match {
  //      case DoxTableAlignment.LEFT   => ColumnType.l(size)
  //      case DoxTableAlignment.RIGHT  => ColumnType.r(size)
  //      case DoxTableAlignment.CENTER => ColumnType.c(size)
  //      case _                        => ColumnType.l(size)
  //    }
  //  }
}