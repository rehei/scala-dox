package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class TableHeadRowKey(config: DoxTableKeyConfig, size: Int) {
  
  assume(size > 0) 
  
  def isMultiColumn() = {
    size > 1
  }
  
}
