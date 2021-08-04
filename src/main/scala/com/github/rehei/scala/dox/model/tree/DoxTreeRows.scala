package com.github.rehei.scala.dox.model.tree

import scala.collection.mutable.ListBuffer
import scala.collection.Seq

trait DoxTreeRows {
  protected def treeRows(doxTree: Seq[DoxTreeItem], traversedEndpoints: ListBuffer[DoxTreeItem], rowBuffer: ListBuffer[Seq[DoxTreeItem]], maxEndpoints: Int): ListBuffer[Seq[DoxTreeItem]] = {
    if (doxTree.isEmpty || (maxEndpoints == traversedEndpoints.length)) {
      rowBuffer
    } else {
      rowBuffer.append(currentRow(doxTree, traversedEndpoints))
      treeRows(nextRow(doxTree), traversedEndpoints, rowBuffer, maxEndpoints)
    }
  }

  protected def currentRow(doxTree: Seq[DoxTreeItem], traversedEndpoints: ListBuffer[DoxTreeItem]) = {
    for (treeItem <- doxTree) yield {
      treeItem match {
        case leaf @ DoxLeaf(_, _)    => endpointCheck(leaf, traversedEndpoints)
        case node @ DoxNode(_)       => node
        case index @ DoxIndexNode(_) => endpointCheck(index, traversedEndpoints)
        case _                       => DoxPlaceholder()
      }
    }
  }

  protected def nextRow(doxTree: Seq[DoxTreeItem]) = {
    doxTree.flatMap(treeItem => if (treeItem.isEndpoint()) { Seq(treeItem) } else { treeItem.children })
  }

  protected def endpointCheck(endpoint: DoxTreeItem, traversedEndpoints: ListBuffer[DoxTreeItem]): DoxTreeItem = {
    if (traversedEndpoints.exists(_ == endpoint)) {
      DoxPlaceholder()
    } else {
      traversedEndpoints.append(endpoint)
      endpoint
    }
  }
}