package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.content.DoxContent

class DoxTableMatrix[T <: AnyRef](protected val model: DoxTable[T]) {

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

  def totalWidth(widthDefault: Double) = {
    dimension().map(_.widthOption.getOrElse(widthDefault)).sum
  }

  def totalSeparatorCount() = {
    dimension().size * 2
  }

  def dimension() = {
    lastHead().map(_.node.config)
  }

  protected def bodyRow(index: Int, row: DoxOption[T]) = {
    row match {
      case DoxValue(content) => Some(DoxValue(convertValue(index + 1, content)))
      case DoxSpace          => Some(DoxSpace)
      case DoxRule           => Some(DoxRule)
      case DoxLegend(_, _)   => None
    }
  }

  protected def legendRow(row: DoxOption[T]) = {
    row match {
      case a: DoxLegend => Some(a)
      case _            => None
    }
  }
  
  protected def convertValue(index: Int, value: T) = {
    for (head <- lastHead()) yield {
      head.node.valueOf(index, value)
    }
  }

  protected def lastHead() = {
    model.head().lastOption.map(_.values).getOrElse(Seq.empty)
  }

  protected def isNonBlank(list: Seq[DoxTableHeadRowKey]) = {
    list
      .filter(_.node.nodeType != DoxTableKeyNodeType.BLANK)
      .size > 0
  }

}