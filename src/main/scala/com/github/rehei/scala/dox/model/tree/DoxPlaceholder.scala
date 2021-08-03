package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.macros.Query

object DoxPlaceholder {
  def apply() = new DoxPlaceholder(DoxTableKeyConfig.NONE)
}
case class DoxPlaceholder(config: DoxTableKeyConfig) extends DoxTreeItem(config) 
