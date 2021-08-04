package com.github.rehei.scala.dox.model.tree

import scala.collection.Seq
import scala.collection.mutable.ListBuffer

import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxNode(config: DoxTableKeyConfig) extends DoxTreeItem() with DoxTreeRows {

  def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }

  def treeRowsSeq() = {
    treeRows(children, ListBuffer[DoxTreeItem](), ListBuffer[Seq[DoxTreeItem]](), endpointsSeq().length)
  }

  def endpointsSeq() = {
    endpointsRecursive(children)
  }

  protected def endpointsRecursive(treeItems: Seq[DoxTreeItem]): Seq[DoxTreeItem] = {
    (for (treeItem <- treeItems) yield {
      treeItem match {
        case leaf: DoxLeaf       => Seq(leaf)
        case node: DoxNode       => endpointsRecursive(node.children)
        case index: DoxIndexNode => Seq(index)
      }
    }).flatten
  }

}