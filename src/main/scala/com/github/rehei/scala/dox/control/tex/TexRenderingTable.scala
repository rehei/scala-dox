package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableHeadRow
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableMatrix

class TexRenderingTable(protected val model: DoxTableMatrix, isInnerTable: Boolean, topRule: Boolean, bottomRule: Boolean, style: TexRenderingStyle) {

  protected case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])

  import com.github.rehei.scala.dox.model.table.content.DoxContent._

  protected val tmpAST = TexAST()
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  protected val tableMarkup = new TexRenderingTableMarkup(model, tmpMarkup)
  protected val tableMode = tableMarkup.getTableMode(isInnerTable)

  def createTableString() = {
    create()
    tmpAST.build()
  }

  protected def create() {
    $ { _ tabular$ & { model.config.computeWidth(model) } { model.config.computeFormatString(model, style) } } {
      if (topRule) {
        tableMode.toprule()
      }
      appendTableHead()
      if (model.head().size > 0) {
        \ midrule;
      }
      appendTableBody()
      if (model.hasLegend) {
        \ midrule;
      }
      appendTableLegend()
      if (bottomRule) {
        tableMode.bottomrule()
      }
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

    val wrappedKey = TexHead(value, style)

    if (value.key.hasNonEmptyChildren) {
      asMappedTableHeadKeyNonEmptyChildren(wrappedKey)
    } else {
      asMappedTableHeadKeyLeaf(wrappedKey)
    }

  }

  protected def asMappedTableHeadKeyNonEmptyChildren(wrappedKey: TexHead) = {
    val ruleOption = {
      for (head <- wrappedKey.value.key.node.textHeadOption) yield {
        val offset = wrappedKey.columnCountOffset
        val target = wrappedKey.columnCountOffset + wrappedKey.columnCount - 1

        \\ cmidrule & { s"${offset}-${target}" }
      }
    }

    val expression = multicolumnWithNormalizedPadding(wrappedKey)

    MappedTableHeadKey(expression, ruleOption)
  }

  protected def asMappedTableHeadKeyLeaf(wrappedKey: TexHead) = {
    val ruleOption = {
      None
    }

    val expression = multicolumnWithNormalizedPadding(wrappedKey)

    MappedTableHeadKey(expression, ruleOption)
  }

  protected def multicolumnWithNormalizedPadding(wrappedKey: TexHead) = {
    val prefix = normalizePaddingString(wrappedKey.value.first)
    val suffix = normalizePaddingString(wrappedKey.value.last)

    \\ multicolumn & { wrappedKey.columnCount } { prefix + wrappedKey.columnAlignmentShort + suffix } { wrappedKey.content }
  }

  protected def normalizePaddingString(isFirstOrLast: Boolean) = {
    if (isFirstOrLast && model.config.fill) {
      "@{}"
    } else {
      ""
    }
  }

  protected def withOffset(input: Seq[DoxTableHeadRowKey]) = {
    for ((row, index) <- input.zipWithIndex) yield {
      val offset = 1 + input.take(index).map(_.size).sum
      val first = (index == 0)
      val last = (index == input.length - 1)
      DoxTableHeadRowKeyWithOffset(offset, row, first, last)
    }
  }

  protected def appendTableBody() {

    for (row <- model.body()) {
      row match {
        case DoxValue(value) => tableMarkup.renderValue(value)
        case DoxRule         => tableMarkup.renderMidRule()
        case DoxSpace        => tableMarkup.renderSpace()
        case DoxLegend(_)    =>
      }
    }
  }

  protected def appendTableLegend() {
    if (model.hasLegend) {
      val legend = new TexRenderingTableLegend("Legende", model.legend()).createTableString()
      \ multicolumn & { model.dimension().drop(1).length } { "l" } { "\\rule{0pt}{.7cm}" + legend }
      \ plain { "\\\\" }
    }
  }

}