package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

case class DoxNode(label: String, children: Seq[DoxTreeItem]) extends DoxTreeItem(label) {

  case class DoxTreeRow(doxTreeRowEntries: Seq[DoxTreeRowEntry])
  case class DoxTreeRowEntry(doxTreeItem: DoxTreeItem, columns: Int)

  case class Rows(currentRowString: Seq[DoxTreeItem], nextRowSeq: ListBuffer[DoxTreeItem])

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

  def getRows(): ListBuffer[Seq[DoxTreeItem]] = {
    val rowBuffer = getRows(nodeChildren, ListBuffer[DoxTreeItem](), ListBuffer[Seq[DoxTreeItem]]())
    rowBuffer
  }
  protected def getRows(doxTree: Seq[DoxTreeItem], addedLeavesList: ListBuffer[DoxTreeItem], allRows: ListBuffer[Seq[DoxTreeItem]]): ListBuffer[Seq[DoxTreeItem]] = {
    if (checkAbortCondition(doxTree, addedLeavesList)) {
      allRows
    } else {
      val row = getRow(doxTree, addedLeavesList)
      val asd = row.currentRowString
      allRows.append(row.currentRowString)
      getRows(row.nextRowSeq, addedLeavesList, allRows)
    }
  }

  protected def checkAbortCondition(doxTree: Seq[DoxTreeItem], addedLeavesList: ListBuffer[DoxTreeItem]) = {
    doxTree.isEmpty || (doxTree.flatMap(_.leaves()).length == addedLeavesList.flatMap(_.leaves()).length)
  }

  protected def getRow(doxTree: Seq[DoxTreeItem], addedLeavesList: ListBuffer[DoxTreeItem]) = {
    val nextRowItems = ListBuffer[DoxTreeItem]()
    val currentRowItems = (for (treeItem <- doxTree) yield {
      getRowEntry(treeItem, nextRowItems, addedLeavesList)
    })
    Rows(currentRowItems, nextRowItems)
  }

  protected def getRowEntry(treeItem: DoxTreeItem, rowBuffer: ListBuffer[DoxTreeItem], addedLeavesList: ListBuffer[DoxTreeItem]) = {
    if (treeItem.isLeaf()) {
      rowBuffer.append(treeItem)
      if (addedLeavesList.exists(_ == treeItem)) {
        DoxLeaf("", "")
      } else {
        addedLeavesList.append(treeItem)
        treeItem
      }
    } else {
      rowBuffer.appendAll(treeItem.nodeChildren())
      treeItem
    }
  }

}