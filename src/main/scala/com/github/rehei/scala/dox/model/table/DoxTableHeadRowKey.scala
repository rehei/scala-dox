package com.github.rehei.scala.dox.model.table

case class DoxTableHeadRowKey(node: DoxTableKeyNode, size: Int, width: Double, hasNonEmptyChildren: Boolean) {

  assume(size > 0)

  def isMultiColumn() = {
    size > 1
  }

}
