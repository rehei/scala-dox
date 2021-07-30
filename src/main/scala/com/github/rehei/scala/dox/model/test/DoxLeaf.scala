package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.macros.Query

object DoxLeaf {
  val NONE = DoxLeaf(DoxTableKeyConfig.NONE, null)
}
case class DoxLeaf(config: DoxTableKeyConfig, property: Query[_]) extends DoxTreeItem(config.text) 
