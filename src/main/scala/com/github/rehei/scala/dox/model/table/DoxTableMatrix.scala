package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.content.DoxContent

class DoxTableMatrix(protected val model: DoxTable[_ <: AnyRef]) {

  import DoxContent._

  def head() = {
    model.head().filter(m => isNonBlank(m.values))
  }

  def body() = {
    (for ((row, index) <- model.data().zipWithIndex) yield {
      bodyRow(index, row)
    }).flatten
  }

  def legend() = {
    (for (row <- model.data()) yield {
      legendRow(row)
    }).flatten
  }

  def totalWidth() = {
    model.root.dimension().width
  }

  def totalSeparatorCount() = {
    dimension().size * 2
  }

  def dimension() = {
    headRowLast().map(_.node)
  }

  protected def headRowLast() = {
    model.head().lastOption.map(_.values).getOrElse(Seq.empty)
  }

  protected def bodyRow(index: Int, row: DoxOption[_]) = {
    row match {
      case DoxValue(content) => Some(DoxValue(convertValue(index + 1, content)))
      case DoxSpace          => Some(DoxSpace)
      case DoxRule           => Some(DoxRule)
      case DoxLegend(_)      => None
    }
  }

  protected def legendRow(row: DoxOption[_]) = {
    row match {
      case a: DoxLegend => Some(a)
      case _            => None
    }
  }

  protected def convertValue(index: Int, value: AnyRef) = {
    for (head <- headRowLast()) yield {
      head.node.valueOf(index, value)
    }
  }

  protected def isNonBlank(list: Seq[DoxTableHeadRowKey]) = {
    list
      .filter(_.node.isHeadDefined())
      .size > 0
  }

}