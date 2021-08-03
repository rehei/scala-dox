package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.macros.Query

case class DoxLeaf(config: DoxTableKeyConfig, propertyQuery: Query[_]) extends DoxTreeItem() 
