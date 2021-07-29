package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer
import com.sun.xml.internal.bind.v2.model.core.LeafInfo

object DoxNode {
  def apply(label: String) = new DoxNode(label, Seq.empty)
}

case class DoxNode(label: String, children: Seq[DoxTreeItem]) extends DoxTreeItem(label) with DoxTreeRows {

  def leafChildren(): Seq[DoxTreeItem] = {
    leafChildren(children)
  }

  def append(doxTreeItem: DoxTreeItem*) {
    this.copy(children = children ++ doxTreeItem)
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