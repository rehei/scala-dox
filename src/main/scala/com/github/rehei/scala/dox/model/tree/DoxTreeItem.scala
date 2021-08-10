package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import scala.collection.Seq
import scala.collection.mutable.ListBuffer

abstract class DoxTreeItem() {

  val config: DoxTableKeyConfig
  val children = ListBuffer[DoxTreeItem]()

  def isEndpoint() = {
    this match {
      case leaf @ DoxLeaf(_, _)    => true
      case index @ DoxIndexNode(_) => true
      case _                       => false
    }
  }

  def endpoints() = {
    this match {
      case leaf @ DoxLeaf(_, _)           => Seq(leaf)
      case node @ DoxNode(_)              => node.endpointsSeq()
      case placeholder @ DoxPlaceholder() => Seq(placeholder)
      case index @ DoxIndexNode(_)        => Seq(index)
    }
  }

  def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }
}

