package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableHeadRow
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.content.DoxContent
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeFormat
import java.text.DecimalFormat

class TexRenderingTable(baseAST: TexAST, protected val model: DoxTableMatrix, isInnerTable: Boolean) {

  import DoxContent._

  protected object ColumnType {
    private val baseString = """\arraybackslash}p"""
    def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
    def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
    def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)
    def numeric(size: Double) = "S[table-number-alignment=center, table-column-width=" + size + "cm]"
    protected def sizeString(size: Double) = {
      val df = new DecimalFormat("#")
      df.setMinimumIntegerDigits(1)
      df.setMaximumFractionDigits(8)
      val stringValue = df.format(size)
      
      "{" + stringValue + "cm}"
    }
  }

  protected case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  protected case class InnerTableOn() extends TableMode {
    def toprule() {
      // nothing
    }
    def bottomrule() {
      cmidrule()
    }
  }
  protected case class InnerTableOff() extends TableMode {
    import tmpMarkup._
    def toprule() {
      \ toprule
    }
    def bottomrule() {
      \ bottomrule
    }
  }

  abstract class TableMode {
    def toprule(): Unit
    def bottomrule(): Unit
  }

  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  protected val tableMode = {
    if (isInnerTable) {
      InnerTableOn()
    } else {
      InnerTableOff()
    }
  }

  import tmpMarkup._

  def createTableString() = {
    create()
    tmpAST.build()
  }

  protected def create() {
    $ { _ tabular$ & { (columnConfigTotalSize()) } { columnConfigEachColumnSize() } } {
      tableMode.toprule()
      appendTableHead()
      \ midrule;
      appendTableBody()
      tableMode.bottomrule()
      appendTableLegend()
    }
  }

  protected def columnConfigTotalSize() = {
    val totalWidth = model.totalWidth()
    val totalSeparatorCount = model.totalSeparatorCount()
    "\\dimexpr(\\tabcolsep*" + totalSeparatorCount + ")+" + totalWidth + "cm"
  }

  protected def columnConfigEachColumnSize() = {
    model.dimension().map(node => getTexAlignment(node.format())).mkString
  }

  protected def appendTableHead() {
    for (row <- model.head()) {
      appendTableHeadRow(row)
    }
  }

  protected def appendTableHeadRow(row: DoxTableHeadRow) = {
    val mappedHead = getMappedHead(row)

    \ plain { mappedHead.map(_.content.generate()).mkString(" & ") + "\\\\" }
    \ plain { mappedHead.flatMap(_.ruleOption).map(_.generate()).mkString(" ") + "\n" }
  }

  protected def getMappedHead(row: DoxTableHeadRow) = {
    withOffset(row.values).map(row => asMappedTableHeadKey(row)).toSeq
  }

  protected def asMappedTableHeadKey(value: DoxTableHeadRowKeyWithOffset) = {

    val offset = value.offset
    val target = value.offset + value.key.size - 1

    val ruleOption = {
      if (value.key.hasNonEmptyChildren) {
        Some(\\ cmidrule & { s"${value.offset}-${target}" })
      } else {
        None
      }
    }
    if (value.key.node.alignment == DoxTableAlignment.ROTATED) {
      MappedTableHeadKey(\\ rotatebox & { 45 } { Text2TEX(false).generate(value.key.node.textHead()) }, ruleOption)
    } else {
      MappedTableHeadKey(\\ multicolumn & { value.key.size } { getHeadAlignment(value.key.node.alignment) } { Text2TEX(false).generate(value.key.node.textHead()) }, ruleOption)
    }
  }

  protected def withOffset(input: Seq[DoxTableHeadRowKey]) = {
    for ((row, index) <- input.zipWithIndex) yield {
      val offset = 1 + input.take(index).map(_.size).sum
      DoxTableHeadRowKeyWithOffset(offset, row)
    }
  }

  protected def appendTableBody() {

    for (row <- model.body()) {
      row match {
        case DoxValue(value) => renderValue(value)
        case DoxRule         => renderRule()
        case DoxSpace        => renderSpace()
        case DoxLegend(_)    =>
      }
    }
  }

  protected def appendTableLegend() {

    for (row <- model.legend()) {
      for (item <- row.content) {
        \ plain { (\\ multicolumn & { model.dimension().drop(1).length } { "l" } { Text2TEX(false).generate(item) }).generate() + "\\\\" + "\n" }
      }
    }
  }

  protected def renderValue(values: Seq[TextAST]) = {
    \ plain { values.map(Text2TEX(false).generate(_)).mkString(" & ") + "\\\\" + "\n" }
  }

  protected def renderSpace() = {
    \ plain { "\\rule{0pt}{3ex}" }
  }

  protected def renderRule() = {
    cmidrule()
  }

  protected def cmidrule() = {
    \ plain { (\\ cmidrule { s"1-${model.dimension().size}" }).generate() + "\n" }
  }

  protected def verticalSpacing() = {
    \ plain { "\n\\vspace*{0.5cm}" + "\n" + "\\\\ \n" }
  }

  protected def getHeadAlignment(alignment: Option[DoxTableAlignment]) = {
    alignment match {
      case Some(DoxTableAlignment.LEFT)    => "l"
      case Some(DoxTableAlignment.RIGHT)   => "r"
      case Some(DoxTableAlignment.CENTER)  => "c"
      case Some(DoxTableAlignment.NUMERIC) => "c"
      case Some(DoxTableAlignment.ROTATED) => "l"
      case _                               => "l"
    }
  }
  protected def getTexAlignment(config: DoxTableKeyNodeFormat) = {
    val size = config.width
    config.alignment match {
      case DoxTableAlignment.LEFT    => ColumnType.l(size)
      case DoxTableAlignment.RIGHT   => ColumnType.r(size)
      case DoxTableAlignment.CENTER  => ColumnType.c(size)
      case DoxTableAlignment.NUMERIC => ColumnType.numeric(size)
      case DoxTableAlignment.ROTATED => ColumnType.l(size)
      case _                         => ColumnType.l(size)
    }
  }

}