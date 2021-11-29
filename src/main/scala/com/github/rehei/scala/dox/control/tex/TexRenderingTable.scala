package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTable

class TexRenderingTable(baseAST: TexAST, floating: Boolean, model: DoxTable[_], reference: String) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])

  protected object ColumnType {
    private val baseString = """\let\newline\\\arraybackslash\hspace{0pt}}m"""
    def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
    def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
    def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)

    private def sizeString(size: Double) = "{" + size + "cm}"
  }

  protected val columnSizeDefault = 2.0

  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  import tmpMarkup._

  def createTableString() = {
    create()
    tmpAST.build()
  }

  protected def create() {

    $ { _ tabular$ & { (columnConfigTotalSize()) } { columnConfigEachColumnSize() } } {
      \ toprule;
      appendTitle()
      appendTableHead()
      \ midrule;
      appendTableBody()
      \ bottomrule;

    }
  }
  protected def appendTitle() = {
    if (!Text2TEX.generate(model.title).isEmpty()) {
      \ plain { (\\ multicolumn & { model.root.leavesRecursive().length } { "c" } { Text2TEX.generate(model.title) }).generate() }
      \ plain { "\\\\" + "\n" }
      \ midrule
    }
  }
  protected def columnConfigTotalSize() = {
    val columnSizes = model.root.leavesRecursive().map(_.config.columnSize.map(size => size).getOrElse(columnSizeDefault))
    val tabColSeps = model.root.leavesRecursive().length * 2

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + columnSizes.sum + "cm"
  }

  protected def columnConfigEachColumnSize() = {
    model.root.leavesRecursive().map(column => getTexAlignment(column.config)).mkString
  }

  protected def appendTableHead() {

    for (row <- model.normal.list()) yield {
      val mappedHead = withOffset(row.values).map(asMappedTableHeadKey(_)).toSeq

      \ plain { mappedHead.map(_.content.generate()).mkString(" & ") + "\\\\" }
      \ plain { mappedHead.flatMap(_.ruleOption).map(_.generate()).mkString(" ") + "\n" }
    }

  }

  protected def asMappedTableHeadKey(value: DoxTableHeadRowKeyWithOffset) = {

    val offset = value.offset
    val target = value.offset + value.key.size - 1

    val ruleOption = {
      if (value.key.rule) {
        Some(\\ cmidrule & { s"${value.offset}-${target}" })
      } else {
        None
      }
    }

    MappedTableHeadKey(\\ multicolumn & { value.key.size } { getTexAlignment(value.key.config) } { Text2TEX.generate(value.key.config.text) }, ruleOption)
  }

  protected def withOffset(input: Seq[DoxTableHeadRowKey]) = {
    var offset = 1
    for (row <- input) yield {
      val result = DoxTableHeadRowKeyWithOffset(offset, row)
      offset = offset + row.size
      result
    }
  }

  protected def appendTableBody() {
    for (row <- model.data) yield {
      \ plain { row.map(Text2TEX.generate(_)).mkString(" & ") + "\\\\" + "\n" }
    }
  }

  protected def getTexAlignment(config: DoxTableKeyConfig) = {
    val size = config.columnSize.getOrElse(columnSizeDefault)
    config.alignment match {
      case DoxTableAlignment.LEFT  => ColumnType.l(size)
      case DoxTableAlignment.RIGHT => ColumnType.r(size)
      case _                       => ColumnType.c(size)
    }
  }

}