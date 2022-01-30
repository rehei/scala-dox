package com.github.rehei.scala.dox.model.table

class DoxTableMatrix(protected val model: DoxTable[_]) {

  def head() = {
    model.head().filter(m => isNonBlank(m.values))
  }

  def body() = {
    model.data()
  }

  def dimension() = {
    model.head().lastOption.map(_.values).getOrElse(Seq.empty).map(_.config)
  }

  protected def isNonBlank(list: Seq[DoxTableHeadRowKey]) = {
    list
      .filter(_.node.nodeType != DoxTableKeyNodeType.BLANK)
      .filter(_.node.nodeType != DoxTableKeyNodeType.WHITESPACE)
      .filter(_.node.nodeType != DoxTableKeyNodeType.COLUMNSPACE)
      .size > 0
  }

}