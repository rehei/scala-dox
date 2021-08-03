package com.github.rehei.scala.dox.model.tree

import scala.collection.Seq
import scala.collection.mutable.ListBuffer

import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxNode(config: DoxTableKeyConfig) extends DoxTreeItem(config) with DoxTreeRows {

  def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }

  def treeRowsSeq(index: Option[DoxTableKeyConfig]) = {
    val rows = treeRows(children, ListBuffer[DoxTreeItem](), ListBuffer[Seq[DoxTreeItem]](), leafChildrenSeq().length)
    if (index.isDefined) {
      val indexedHeadRow = rows.headOption.map(headRow => DoxPlaceholder(index.get) +: headRow).getOrElse(Seq[DoxTreeItem]())
      rows.drop(1)
        .map(headRow => DoxPlaceholder() +: headRow)
        .prepend(indexedHeadRow)
    }
    rows
  }

  def leafChildrenSeq() = {
    leafChildren(children)
  }

  protected def leafChildren(treeItems: Seq[DoxTreeItem]): Seq[DoxLeaf] = {
    if (treeItems.isEmpty) {
      Seq.empty
    } else {
      (for (treeItem <- treeItems) yield {
        treeItem match {
          case leaf: DoxLeaf => Seq(leaf)
          case node: DoxNode => leafChildren(node.children)
        }
      }).flatten
    }
  }

}