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

  def treeRowsSeq(index: Option[DoxTableKeyConfig]) = {
    val rows = treeRows(children, ListBuffer[DoxTreeItem](), ListBuffer[Seq[DoxTreeItem]](), leafChildrenSeq().length)
    index match {
      case Some(config) => indexTreeRows(rows, config)
      case None         => rows
    }
  }

  def leafChildrenSeq() = {
    leafChildren(children)
  }

  protected def indexTreeRows(rows: ListBuffer[Seq[DoxTreeItem]], index: DoxTableKeyConfig) = {
    val indexedHeadRow = rows.headOption.map(headRow => DoxPlaceholder(index) +: headRow).getOrElse(Seq[DoxTreeItem]())
    rows.drop(1)
      .map(headRow => DoxPlaceholder() +: headRow)
      .prepend(indexedHeadRow)
    rows
  }
  
  protected def leafChildren(treeItems: Seq[DoxTreeItem]): Seq[DoxLeaf] = {
    (for (treeItem <- treeItems) yield {
      treeItem match {
        case leaf: DoxLeaf => Seq(leaf)
        case node: DoxNode => leafChildren(node.children)
      }
    }).flatten
  }

}