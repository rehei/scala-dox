package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigExtended
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.text.TextFactory

class TexRenderingTableTransposed(baseAST: TexAST, model: DoxTable[_], isInnerTable: Boolean) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  case class TableContent(contentHeadOffset: TexCommandInline, contentHead: String, contentData: Seq[TextAST])
  case class TableConfig(categoryWidth: Double, dataWidth: Double, hasSpacing: Boolean)
  protected object ColumnType {
    private val baseString = """\let\newline\\\arraybackslash\hspace{0pt}}p"""
    def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
    def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
    def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)
    def numeric(size: Double) = "S[table-column-width=" + size + "cm]"
    private def sizeString(size: Double) = "{" + size + "cm}"
  }
  protected val columnSizeDefault = 3.0
  protected val columnSizeCategory = 4.0
  protected val dataColumnAmount = model.transposed.list().map(_.data.length).max
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  protected val transposedConfig = tableConfig()
  protected val modelTransposed = model.transposed
  protected val ROW_SPACING = {
    if (transposedConfig.hasSpacing) {
      "\\rule{0pt}{4ex}"
    } else {
      ""
    }
  }
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
    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + (transposedConfig.categoryWidth + dataColumnAmount * transposedConfig.dataWidth) + "cm"
  }
  protected def columnConfigEachColumnSize() = {
    ColumnType.l(transposedConfig.categoryWidth) ++ (1 to dataColumnAmount).map(col => getTableAlignment()).mkString
  }

  protected def appendTitle() = {
    if (showTitle) {
      \ plain { (\\ multicolumn & { dataColumnAmount + 1 } { "c" } { titleAST() }).generate() }
      \ plain { "\\\\" + "\n" }
      \ midrule
    }
  }

  protected def titleAST() = {
    model.headTitle.map(
      title => {
        styleText(title)
      }).getOrElse("")
  }
  protected def tableConfig() = {
    TableConfig(
      model.root.config.width.getOrElse(columnSizeCategory),
      model.root.config.transposedWidth.getOrElse(columnSizeDefault),
      model.root.config.columnSpacing)
  }

  protected def showTitle() = {
    model.headTitle.isDefined && !isInnerTable
  }

  protected def appendTable() {
    val rows = content()
    rows.headOption.map {
      head =>
        {
          writeContent(head)
          for (row <- rows.drop(1)) {
            if (transposedConfig.hasSpacing) {
              \ midrule
            }
            writeContent(row)
          }
        }
    }

  }

  protected def writeContent(row: TableContent) = {
    \ plain { ROW_SPACING + row.contentHeadOffset.generate() }
    \ plain { row.contentHead + " & " }
    \ plain { row.contentData.map(data => Text2TEX.generate(data)).mkString(" & ") + "\\\\" + "\n" }
  }

  protected def content() = {
    for (row <- modelTransposed.list()) yield {
      TableContent(\\ hspace { (row.columnDepth * 5) + "mm" }, styleText(row.head), row.data)
    }
  }
  protected def styleText(config: DoxTableKeyConfigExtended) = {
    config.base.style.applyStyle(Text2TEX.generate(config.base.text))
  }
  protected def getTableAlignment() = {
    model.root.config.base.alignment match {
      case DoxTableAlignment.LEFT    => ColumnType.l(transposedConfig.dataWidth)
      case DoxTableAlignment.RIGHT   => ColumnType.r(transposedConfig.dataWidth)
      case DoxTableAlignment.CENTER  => ColumnType.c(transposedConfig.dataWidth)
      case DoxTableAlignment.NUMERIC => ColumnType.numeric(transposedConfig.dataWidth)
      case _                         => ColumnType.r(transposedConfig.dataWidth)
    }
  }
}