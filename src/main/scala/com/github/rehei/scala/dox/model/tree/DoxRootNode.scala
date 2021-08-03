package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigSupport

object DoxRootNode {
  def apply(rootConfig: DoxTableConfig) = new DoxRootNode(rootConfig)
}

class DoxRootNode(val rootConfig: DoxTableConfig) extends DoxNode(DoxTableKeyConfigSupport.NONE) {

  override def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }
}