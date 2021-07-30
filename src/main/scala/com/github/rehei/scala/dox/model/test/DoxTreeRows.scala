package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer

trait DoxTreeRows {
  protected def treeRows(doxTree: Seq[DoxTreeItem], traversedLeaves: ListBuffer[DoxLeaf], rowBuffer: ListBuffer[Seq[DoxTreeItem]], maxLeaves: Int): ListBuffer[Seq[DoxTreeItem]] = {
    if (doxTree.isEmpty || (maxLeaves == traversedLeaves.length)) {
      rowBuffer
    } else {
      rowBuffer.append(currentRow(doxTree, traversedLeaves))
      treeRows(nextRow(doxTree), traversedLeaves, rowBuffer,maxLeaves)
    }
  }

  protected def currentRow(doxTree: Seq[DoxTreeItem], traversedLeaves: ListBuffer[DoxLeaf]): Seq[DoxTreeItem] = {
    for (treeItem <- doxTree) yield {
      treeItem match {
        case leaf @ DoxLeaf(_,_) => leafEntryCheck(leaf, traversedLeaves)
        case node @ DoxNode(_) => node
      }
    }
  }

  protected def nextRow(doxTree: Seq[DoxTreeItem]) = {
    doxTree.flatMap(treeItem => if (treeItem.isLeaf()) { Seq(treeItem) } else { treeItem.nodeChildren() })
  }

  protected def leafEntryCheck(leaf: DoxLeaf, traversedLeaves: ListBuffer[DoxLeaf]) = {
    if (traversedLeaves.exists(_ == leaf)) {
      DoxLeaf.NONE
    } else {
      traversedLeaves.append(leaf)
      leaf
    }
  }
}