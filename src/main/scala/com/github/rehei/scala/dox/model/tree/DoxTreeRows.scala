package com.github.rehei.scala.dox.model.tree

import scala.collection.mutable.ListBuffer
import scala.collection.Seq

trait DoxTreeRows {
  protected def treeRows(doxTree: Seq[DoxTreeItem], traversedLeaves: ListBuffer[DoxTreeItem], rowBuffer: ListBuffer[Seq[DoxTreeItem]], maxLeaves: Int): ListBuffer[Seq[DoxTreeItem]] = {
    if (doxTree.isEmpty || (maxLeaves == traversedLeaves.length)) {
      rowBuffer
    } else {
      rowBuffer.append(currentRow(doxTree, traversedLeaves))
      treeRows(nextRow(doxTree), traversedLeaves, rowBuffer, maxLeaves)
    }
  }

  protected def currentRow(doxTree: Seq[DoxTreeItem], traversedLeaves: ListBuffer[DoxTreeItem]): Seq[DoxTreeItem] = {
    for (treeItem <- doxTree) yield {
      treeItem match {
        case leaf @ DoxLeaf(_, _) => leafEntryCheck(leaf, traversedLeaves)
        case node @ DoxNode(_)    => node
      }
    }
  }

  protected def nextRow(doxTree: Seq[DoxTreeItem]) = {
    doxTree.flatMap(treeItem => if (treeItem.isLeaf()) { Seq(treeItem) } else { treeItem.children })
  }

  protected def leafEntryCheck(leaf: DoxLeaf, traversedLeaves: ListBuffer[DoxTreeItem]): DoxTreeItem = {
    if (traversedLeaves.exists(_ == leaf)) {
      DoxPlaceholder()
    } else {
      traversedLeaves.append(leaf)
      leaf
    }
  }
}