package com.github.rehei.scala.dox.model.tree

import scala.collection.Seq
import scala.collection.mutable.ListBuffer

import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxNode(config: DoxTableKeyConfig, rootConfig: Option[DoxTableConfig] = None) extends DoxTreeItem(config.text, config) with DoxTreeRows {

  val children = ListBuffer[DoxTreeItem]()

  def withTableConfig(tableConfig: DoxTableConfig) = {
    this.copy(rootConfig = Some(tableConfig))
  }

  def addNodes(doxTreeItem: DoxTreeItem*) = {
    children.appendAll(doxTreeItem)
    this
  }

  def treeRowsSeq(index: Option[DoxTableKeyConfig]) = {
    val rows = treeRows(children, ListBuffer[DoxLeaf](), ListBuffer[Seq[DoxTreeItem]](), leafChildrenSeq().length)
    if (index.isDefined) {
      val indexedHeadRow = rows.headOption.map(headRow => DoxLeaf(index.get, null) +: headRow).getOrElse(Seq[DoxTreeItem]())
      rows.drop(1)
        .map(headRow => DoxLeaf.NONE +: headRow)
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