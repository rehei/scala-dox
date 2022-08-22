package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableHeadRow
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.content.DoxContent
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeFormat
import java.text.DecimalFormat
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode
import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeAlignment

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
      df.setMaximumFractionDigits(3)
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
    model.dimension().map(node => getTexAlignment(node)).mkString
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
    val text = Text2TEX(false).generate(value.key.node.textHead())

    val content = {
      if (value.key.node.format.isRotated) {
        (\\ rotatebox & { 45 } { text }).generate()
      } else {
        text
      }
    }

    val expression = {
      \\ multicolumn & { value.key.size } { getHeadAlignment(value.key.node) } { content }
    }

    MappedTableHeadKey(expression, ruleOption)

  }

  protected def rotate(enable: Boolean, expression: String) = {
    if (enable) {
      "\\rotatebox{45}{" + expression + "}"
    } else {
      expression
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

  protected def getHeadAlignment(node: DoxTableKeyNode) = {
    node.format.alignment match {
      case DoxTableKeyNodeAlignment.LEFT    => "l"
      case DoxTableKeyNodeAlignment.RIGHT   => "r"
      case DoxTableKeyNodeAlignment.CENTER  => "c"
      case DoxTableKeyNodeAlignment.NUMERIC => "c"
      case _                                => throw new RuntimeException("This should not happen")
    }
  }
  protected def getTexAlignment(node: DoxTableKeyNode) = {
    val size = node.dimension().width
    node.format.alignment match {
      case DoxTableKeyNodeAlignment.LEFT    => ColumnType.l(size)
      case DoxTableKeyNodeAlignment.RIGHT   => ColumnType.r(size)
      case DoxTableKeyNodeAlignment.CENTER  => ColumnType.c(size)
      case DoxTableKeyNodeAlignment.NUMERIC => ColumnType.numeric(size)
      case _                                => throw new RuntimeException("This should not happen")
    }
  }

}