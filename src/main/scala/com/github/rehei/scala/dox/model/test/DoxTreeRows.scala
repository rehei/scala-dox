package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

trait DoxTreeRows {
  protected def treeRows(doxTree: Seq[DoxTreeItem], addedLeaves: ListBuffer[DoxTreeItem], allRows: ListBuffer[Seq[DoxTreeItem]]): ListBuffer[Seq[DoxTreeItem]] = {
    if (checkAbortCondition(doxTree, addedLeaves)) {
      allRows
    } else {
      allRows.append(currentRow(doxTree, addedLeaves))
      treeRows(nextRow(doxTree), addedLeaves, allRows)
    }
  }

  protected def checkAbortCondition(doxTree: Seq[DoxTreeItem], addedLeaves: ListBuffer[DoxTreeItem]) = {
    doxTree.isEmpty || (doxTree.flatMap(_.leaves()).length == addedLeaves.flatMap(_.leaves()).length)
  }

  protected def currentRow(doxTree: Seq[DoxTreeItem], addedLeaves: ListBuffer[DoxTreeItem]): Seq[DoxTreeItem] = {
    for (treeItem <- doxTree) yield {
      treeItem match {
        case leaf @ DoxLeaf(_, _) => leafEntryCheck(leaf, addedLeaves)
        case node @ DoxNode(_, _) => node
      }
    }
  }

  protected def nextRow(doxTree: Seq[DoxTreeItem]) = {
    doxTree.flatMap(treeItem => if (treeItem.isLeaf()) { Seq(treeItem) } else { treeItem.nodeChildren() })
  }

  protected def leafEntryCheck(leaf: DoxLeaf, addedLeaves: ListBuffer[DoxTreeItem]) = {
    if (addedLeaves.exists(_ == leaf)) {
      DoxLeaf("", "")
    } else {
      addedLeaves.append(leaf)
      leaf
    }
  }
}