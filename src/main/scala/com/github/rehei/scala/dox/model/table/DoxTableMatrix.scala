package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.content.DoxContent

class DoxTableMatrix[T <: AnyRef](protected val model: DoxTable[T]) {

  import DoxContent._

  def head() = {
    model.head().filter(m => isNonBlank(m.values))
  }

  def body() = {
    for ((row, index) <- model.data().zipWithIndex) yield {
      convertRow(index, row)
    }
  }

  def dimension() = {
    lastHead().map(_.config)
  }

  protected def convertRow(index: Int, row: DoxOption[T]) = {
    row match {
      case DoxValue(content) => DoxValue(convertValue(index + 1, content))
      case DoxSpace          => DoxSpace
      case DoxRule           => DoxRule
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