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
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.DoxTableConfig

class TexRenderingTable(protected val model: DoxTableMatrix, isInnerTable: Boolean, style: TexRenderingStyle) {

  import DoxContent._

  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  protected case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  protected case class InnerTableOn() extends TableMode {
    def toprule() {
      // nothing
    }
    def midrule() {
      if (model.hasLegend) {
        cmidrule()
      }
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
    def midrule() {
      if (model.hasLegend) {
        cmidrule()
      }
    }
    def bottomrule() {
      \ bottomrule
    }
  }

  abstract class TableMode {
    def toprule(): Unit
    def midrule(): Unit
    def bottomrule(): Unit
  }

  protected val tableMode = {
    if (isInnerTable) {
      InnerTableOn()
    } else {
      InnerTableOff()
    }
  }

  def createTableString() = {
    create()
    tmpAST.build()
  }

  protected def create() {
    $ { _ tabular$ & { model.config.computeWidth(model) } { model.config.computeFormatString(model) } } {
      tableMode.toprule()
      appendTableHead()
      \ midrule;
      appendTableBody()
      tableMode.midrule()
      appendTableLegend()
      tableMode.bottomrule()
    }
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

    val textFormatted = {
      if (value.key.node.format.isRotated) {
        (\\ rotatebox & { 45 } { text }).generate()
      } else {
        getHeadAlignmentMinipage(value.key.node, text)
      }
    }
    val size = {
      if (value.key.size > 1) {
        value.key.size
      } else {
        0
      }
    }
    val multicolumnWidth = "\\dimexpr(\\tabcolsep*" + size + ")+" + value.key.width + "cm"

    val expression = {
      if (value.key.hasNonEmptyChildren) {
        \\ multicolumn & { value.key.size } { "c" } { style.get(multicolumnWidth, textFormatted) }
      } else {
        \\ multicolumn & { value.key.size } { getHeadAlignment(value.key.node) } { style.get(multicolumnWidth, textFormatted) }
      }
    }

    MappedTableHeadKey(expression, ruleOption)

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
    var isFirst = true
    for (row <- model.legend()) {
      for (item <- row.content) {
        if (isFirst) {
          legendContent(Text2TEX(false).generate(TextFactory.text("Legende: ").append(item)), isFirst)
        } else {
          legendContent(legendPlaceholderSpace() + Text2TEX(false).generate((item)), isFirst)
        }
        isFirst = false
      }
    }
  }

  protected def legendContent(content: String, isFirst: Boolean) {
    if (isFirst) {
      \ plain { (\\ multicolumn & { model.dimension().drop(1).length } { "l" } { "\\rule{0pt}{.7cm}\\scriptsize \\textit {" + content + "}" }).generate() + "\\\\" + "\n" }
    } else {
      \ plain { (\\ multicolumn & { model.dimension().drop(1).length } { "l" } { "\\scriptsize \\textit {" + content + "}" }).generate() + "\\\\" + "\n" }
    }
  }

  protected def legendPlaceholderSpace() = {
    \ hspace { "3.6em" }
  }

  protected def legendVerticalOffset() = {
    \ vspace { "0.2cm" }
  }

  protected def renderValue(values: Seq[TextAST]) = {
    \ plain { values.map(Text2TEX(false).generate(_)).mkString(" & ") + "\\\\" + "\n" }
  }

  protected def renderSpace() = {
    \ rule & { "0pt" } { "3ex" } 
  }

  protected def renderRule() = {
    cmidrule()
  }

  protected def cmidrule() = {
    \ plain { (\\ cmidrule { s"1-${model.dimension().size}" }).generate() + "\n" }
  }

  protected def getHeadAlignmentMinipage(node: DoxTableKeyNode, text: String) = {
    node.format.alignment match {
      case DoxTableKeyNodeAlignment.LEFT    => ColumnType.lMinipage(text)
      case DoxTableKeyNodeAlignment.RIGHT   => ColumnType.rMinipage(text)
      case DoxTableKeyNodeAlignment.CENTER  => ColumnType.cMinipage(text)
      case DoxTableKeyNodeAlignment.NUMERIC => ColumnType.cMinipage(text)
      case _                                => throw new RuntimeException("This should not happen")
    }
  }
  
  protected def getHeadAlignmentNoSize(node: DoxTableKeyNode) = {
    node.format.alignment match {
      case DoxTableKeyNodeAlignment.LEFT    => ColumnType.l()
      case DoxTableKeyNodeAlignment.RIGHT   => ColumnType.r()
      case DoxTableKeyNodeAlignment.CENTER  => ColumnType.c()
      case DoxTableKeyNodeAlignment.NUMERIC => ColumnType.c()
      case _                                => throw new RuntimeException("This should not happen")
    }
  }
  
  protected def getHeadAlignment(node: DoxTableKeyNode) = {
    val size = node.dimension().width
    node.format.alignment match {
      case DoxTableKeyNodeAlignment.LEFT    => ColumnType.l(size)
      case DoxTableKeyNodeAlignment.RIGHT   => ColumnType.r(size)
      case DoxTableKeyNodeAlignment.CENTER  => ColumnType.c(size)
      case DoxTableKeyNodeAlignment.NUMERIC => ColumnType.c(size)
      case _                                => throw new RuntimeException("This should not happen")
    }
  }

}