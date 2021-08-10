package com.github.rehei.scala.dox.model.table.tree

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxTableTreeHeadRowKey(config: DoxTableKeyConfig, size: Int, rule: Boolean) {
  
  assume(size > 0) 
  
  def isMultiColumn() = {
    size > 1
  }
  
}
