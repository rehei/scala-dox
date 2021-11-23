package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTable

class TexRenderingTable2(baseAST: TexAST, floating: Boolean, model: DoxTable[_], reference: String) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  case class TableContent(contentHead: TexCommandInline, contentData: TexCommandInline)

  protected object ColumnType {
    private val baseString = """\let\newline\\\arraybackslash\hspace{0pt}}m"""
    def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
    def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
    def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)

    private def sizeString(size: Double) = "{" + size + "cm}"
  }
  protected val headColumns = 5
  protected val columnSizeDefault = 2.0

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
    $ { _ table & { ###("H!") } } {
      \ centering;
      $ { _ tabular$ & { (columnConfigTotalSize()) } { columnConfigEachColumnSize() } } {
        \ toprule;
        appendTableHead()
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
    val head = 5
    val body = 5
    val columnSizes = model.root.leavesRecursive().map(_.config.columnSize.map(size => size).getOrElse(columnSizeDefault))
    val tabColSeps = model.root.leavesRecursive().length * 2

    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + (head + body * 2) + "cm"
  }
  protected def columnConfigEachColumnSize() = {
    model.root.leavesRecursive().map(column => getTexAlignment(column.config)).mkString
  }

  protected def appendTable() = {
    val transposed = model.transposed
  }

  protected def appendTableHead() {
    val rows = getcontent()
    for (row <- rows) yield {
      println(row.contentHead)
      \ plain { row.contentHead.generate() }
      //      \ plain { row.contentHead.generate().mkString(" & ") + row.contentData.generate() }
      //
      //    for (row <- model.head.list()) yield {
      //      val mappedHead = withOffset(row.values).map(asMappedTableHeadKey(_)).toSeq
      //      \ plain { mappedHead.map(_.content.generate()).mkString(" & ") + "\\\\" }
      //      \ plain { mappedHead.flatMap(_.ruleOption).map(_.generate()).mkString(" ") + "\n" }
    }

  }
  protected def getcontent() = {
    for (row <- model.transposed.list()) yield {

      val columns = headColumns - row.columnDepth
      TableContent(\\ multicolumn & { columns } { ColumnType.l(columns * 2) } { Text2TEX.generate(row.head) }, \\ multicolumn & { 5 } { row.data.map(Text2TEX.generate(_)).mkString(" & ") + "\\\\" + "\n" })
    }
  }

  //  protected def createT(child: DoxTableKeyNode, multiSize: Int) = {
  //    if (child.children.size == 0) {
  //      \ plain { asTableHeadKey(child, multiSize).content.generate().mkString(" & ") }
  //    } else {
  //
  //    }
  //  }
  protected def appendTableBody() {
    for (row <- model.data) yield {
      \ plain { row.map(Text2TEX.generate(_)).mkString(" & ") + "\\\\" + "\n" }
    }
  }
  //  protected def asTableHeadKey(value: DoxTableKeyNode, columns: Int) = {
  //    TableHeadKey(\\ multicolumn & { columns } { ColumnType.l(2 * columns) } { Text2TEX.generate(value.config.text) })
  //  }

  protected def asMappedTableHeadKey(value: DoxTableHeadRowKeyWithOffset) = {

    val offset = value.offset
    val target = value.offset + value.key.size - 1

    val ruleOption = {
      if (value.key.rule) {
        Some(\\ cmidrule & { s"${value.offset}-${target}" })
        //        Some("\\cmidrule & { s"${value.offset}-${target}" })
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

  //  protected def appendTableBody() {
  //    for (row <- model.data) yield {
  //      \ plain { row.map(Text2TEX.generate(_)).mkString(" & ") + "\\\\" + "\n" }
  //    }
  //  }

  protected def getTexAlignment(config: DoxTableKeyConfig) = {
    val size = config.columnSize.getOrElse(columnSizeDefault)
    config.alignment match {
      case DoxTableAlignment.LEFT  => ColumnType.l(size)
      case DoxTableAlignment.RIGHT => ColumnType.r(size)
      case _                       => ColumnType.c(size)
    }
  }

}