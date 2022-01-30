package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableHeadRow
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigExtended
import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.content.DoxContent

class TexRenderingTable(baseAST: TexAST, protected val model: DoxTableMatrix[_], isInnerTable: Boolean) {

  import DoxContent._
  
  protected object ColumnType {
    private val baseString = """\arraybackslash}p"""
    def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
    def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
    def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)
    def numeric(size: Double) = "S[table-number-alignment=center, table-column-width=" + size + "cm]"
    private def sizeString(size: Double) = "{" + size + "cm}"
  }

  protected case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])
  protected case class InnerTableOn(factory: TexMarkupFactory) extends TableMode {
    import factory._
    def toprule() {
      // nothing
    }
    def bottomrule() {
      cmidrule()
    }
  }
  protected case class InnerTableOff(factory: TexMarkupFactory) extends TableMode {
    import factory._
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

  protected val COLUMN_SIZE_DEFAULT = 2.0
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  protected val tableMode = {
    if (isInnerTable) {
      InnerTableOn(tmpMarkup)
    } else {
      InnerTableOff(tmpMarkup)
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
    }
  }

  protected def columnConfigTotalSize() = {
    val columnSizes = model.dimension().map(_.widthOption.getOrElse(COLUMN_SIZE_DEFAULT))
    val tabColSeps = model.dimension().size * 2
    "\\dimexpr(\\tabcolsep*" + tabColSeps + ")+" + columnSizes.sum + "cm"
  }

  protected def columnConfigEachColumnSize() = {
    model.dimension().map(config => getTexAlignment(config)).mkString
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
    if (value.key.config.base.alignment == DoxTableAlignment.ROTATED) {
      MappedTableHeadKey(\\ rotatebox & { 45 } { Text2TEX(false).generate(value.key.config.base.text) }, ruleOption)
    } else {
      MappedTableHeadKey(\\ multicolumn & { value.key.size } { getHeadAlignment(value.key.config) } { Text2TEX(false).generate(value.key.config.base.text) }, ruleOption)
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

  protected def getHeadAlignment(config: DoxTableKeyConfigExtended) = {
    config.base.alignment match {
      case DoxTableAlignment.LEFT    => "l"
      case DoxTableAlignment.RIGHT   => "r"
      case DoxTableAlignment.CENTER  => "c"
      case DoxTableAlignment.NUMERIC => "c"
      case DoxTableAlignment.ROTATED => "l"
      case _                         => "l"
    }
  }
  protected def getTexAlignment(config: DoxTableKeyConfigExtended) = {
    val size = config.widthOption.getOrElse(COLUMN_SIZE_DEFAULT)
    config.base.alignment match {
      case DoxTableAlignment.LEFT    => ColumnType.l(size)
      case DoxTableAlignment.RIGHT   => ColumnType.r(size)
      case DoxTableAlignment.CENTER  => ColumnType.c(size)
      case DoxTableAlignment.NUMERIC => ColumnType.numeric(size)
      case DoxTableAlignment.ROTATED => ColumnType.l(size)
      case _                         => ColumnType.l(size)
    }
  }

}