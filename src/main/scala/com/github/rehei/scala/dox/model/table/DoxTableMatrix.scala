package com.github.rehei.scala.dox.model.table

class DoxTableMatrix(protected val model: DoxTable[_]) {

  import model._

  def head() = {
    model.head().filter(m => isNonBlank(m.values))
  }

  def body() = {

    for ((row, index) <- model.data().zipWithIndex) yield {
      convertRow(index, row)
    }
  }

  protected def convertRow(index: Int, row: DoxOption[_]) = {
    row match {
      case DoxValue(content) => DoxValue(convertValue(index + 1, content))
      case DoxSpace          => DoxSpace
      case DoxRule           => DoxRule
    }
  }

  protected def convertValue(index: Int, value: AnyRef) = {
    for (head <- lastHead()) yield {
      head.node.valueOf(index, value)
    }
  }

  def dimension() = {
    lastHead().map(_.config)
  }

  protected def lastHead() = {
    model.head().lastOption.map(_.values).getOrElse(Seq.empty)
  }

  protected def isNonBlank(list: Seq[DoxTableHeadRowKey]) = {
    list
      .filter(_.node.nodeType != DoxTableKeyNodeType.BLANK)
      .filter(_.node.nodeType != DoxTableKeyNodeType.WHITESPACE)
      .filter(_.node.nodeType != DoxTableKeyNodeType.COLUMNSPACE)
      .size > 0
  }

}