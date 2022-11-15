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

  protected case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])

  import DoxContent._

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

    val wrappedKey = TexHead(value, style)

    if (value.key.hasNonEmptyChildren) {
      asMappedTableHeadKeyNonEmptyChildren(wrappedKey)
    } else {
      asMappedTableHEadKeyLeaf(wrappedKey)
    }

  }

  protected def asMappedTableHeadKeyNonEmptyChildren(wrappedKey: TexHead) = {
    val ruleOption = {
      val offset = wrappedKey.columnCountOffset
      val target = wrappedKey.columnCountOffset + wrappedKey.columnCount - 1

      Some(\\ cmidrule & { s"${offset}-${target}" })
    }

    val expression = {
      \\ multicolumn & { wrappedKey.columnCount } { "c" } { wrappedKey.content }
    }

    MappedTableHeadKey(expression, ruleOption)
  }

  protected def asMappedTableHEadKeyLeaf(wrappedKey: TexHead) = {
    val ruleOption = {
      None
    }

    val expression = {
      \\ multicolumn & { wrappedKey.columnCount } { "c" } { wrappedKey.content }
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
        case DoxValue(value) => tableMarkup.renderValue(value)
        case DoxRule         => tableMarkup.renderMidRule()
        case DoxSpace        => tableMarkup.renderSpace()
        case DoxLegend(_)    =>
      }
    }
  }

  protected def appendTableLegend() {
    if (model.hasLegend) {
      val legend = new TexRenderingTableLegend(model, "Legende").createTableString()
      \ multicolumn & { model.dimension().drop(1).length } { "l" } { "\\rule{0pt}{.7cm}" + legend }
      \ plain { "\\\\" }
    }
  }

}