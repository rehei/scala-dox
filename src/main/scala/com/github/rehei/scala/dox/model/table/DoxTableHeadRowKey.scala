package com.github.rehei.scala.dox.model.table

case class DoxTableHeadRowKey(config: DoxTableKeyConfigExtended, size: Int, rule: Boolean) {

  assume(size > 0)

  def isMultiColumn() = {
    size > 1
  }

}
