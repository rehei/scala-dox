package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

object DoxNode {
  def apply(label: String) = new DoxNode(label, Seq.empty)
}
case class DoxNode(label: String, children: Seq[DoxTreeItem]) extends DoxTreeItem(label) with DoxTreeRows {

  def leafChildren(treeItems: Seq[DoxTreeItem] = children): Seq[DoxTreeItem] = {
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

  def append(doxTreeItem: DoxTreeItem) {
    this.copy(children = children ++ Seq(doxTreeItem))
  }

  def treeRows(): ListBuffer[Seq[DoxTreeItem]] = {
    treeRows(nodeChildren, ListBuffer[DoxTreeItem](), ListBuffer[Seq[DoxTreeItem]]())
  }

}