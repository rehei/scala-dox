package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigSupport

class DoxRootNode(val caption: String) extends DoxNode(DoxTableKeyConfigSupport.NONE) {

  override def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }

}