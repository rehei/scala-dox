package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer
import com.sun.xml.internal.bind.v2.model.core.LeafInfo
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxNode(config:DoxTableKeyConfig) extends DoxTreeItem(config.text) with DoxTreeRows {
  val children = ListBuffer[DoxTreeItem]()
  
  def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }

  def withType[T]() = {
    
  }
  
  def treeRows(): ListBuffer[Seq[DoxTreeItem]] = {
    treeRows(children, ListBuffer[DoxLeaf](), ListBuffer[Seq[DoxTreeItem]](), leafChildren().length)
  }

  def leafChildren(): Seq[DoxTreeItem] = {
    leafChildren(children)
  }

  protected def leafChildren(treeItems: Seq[DoxTreeItem]): Seq[DoxTreeItem] = {
    if (treeItems.isEmpty) {
      Seq.empty
    } else {
      if (!treeItems.head.isLeaf()) {
        leafChildren(treeItems.head.nodeChildren()) ++ leafChildren(treeItems.drop(1))
      } else {
        treeItems.takeWhile(_.isLeaf()) ++ leafChildren(treeItems.dropWhile(_.isLeaf()))
      }
    }
  }
}