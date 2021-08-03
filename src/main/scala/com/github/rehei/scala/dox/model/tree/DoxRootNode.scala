package com.github.rehei.scala.dox.model.tree

import scala.collection.Seq
import scala.collection.mutable.ListBuffer

import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

object DoxRootNode{
  def apply(config:DoxTableKeyConfig, rootConfig: DoxTableConfig) = new DoxRootNode(config,rootConfig)
}

class DoxRootNode(override val config: DoxTableKeyConfig, val rootConfig: DoxTableConfig) extends DoxNode(config) {
  override def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }
}