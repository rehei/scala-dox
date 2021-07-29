package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

trait DoxTreeRows {
  protected def treeRows(doxTree: Seq[DoxTreeItem], addedLeaves: ListBuffer[DoxLeaf], allRows: ListBuffer[Seq[DoxTreeItem]], maxLeaves: Int): ListBuffer[Seq[DoxTreeItem]] = {
    if (doxTree.isEmpty || (maxLeaves == addedLeaves.length)) {
      allRows
    } else {
      allRows.append(currentRow(doxTree, addedLeaves))
      treeRows(nextRow(doxTree), addedLeaves, allRows,maxLeaves)
    }
  }

  protected def currentRow(doxTree: Seq[DoxTreeItem], addedLeaves: ListBuffer[DoxLeaf]): Seq[DoxTreeItem] = {
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

  protected def leafEntryCheck(leaf: DoxLeaf, addedLeaves: ListBuffer[DoxLeaf]) = {
    if (addedLeaves.exists(_ == leaf)) {
      DoxLeaf("", "")
    } else {
      addedLeaves.append(leaf)
      leaf
    }
  }
}