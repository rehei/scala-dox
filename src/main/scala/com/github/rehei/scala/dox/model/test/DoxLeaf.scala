package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion

object DoxLeaf {
  val NONE = DoxLeaf(DoxTableKeyConfig.NONE, null)
}
case class DoxLeaf(config: DoxTableKeyConfig, property: Query[_]) extends DoxTreeItem(config.text.sequence.mkString(" ")) 
