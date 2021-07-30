package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.macros.Query

//case class DoxLeaf(config: DoxTableKeyConfig, property: Query[_]) extends DoxTreeItem(config.text.sequence.mkString(" ")) {
case class DoxLeaf(config: DoxTableKeyConfig, property: Query[_]) extends DoxTreeItem(config.text.sequence.mkString(" ")) {
}
