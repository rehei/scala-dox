package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer
import com.sun.xml.internal.bind.v2.model.core.LeafInfo

case class DoxNode(label: String) extends DoxTreeItem(label) with DoxTreeRows {
  val children = ListBuffer[DoxTreeItem]()
  
  def leafChildren(): Seq[DoxTreeItem] = {
    leafChildren(children)
  }

  def addNode(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }

  def treeRows(): ListBuffer[Seq[DoxTreeItem]] = {
    treeRows(children, ListBuffer[DoxLeaf](), ListBuffer[Seq[DoxTreeItem]](), leafChildren().length)
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