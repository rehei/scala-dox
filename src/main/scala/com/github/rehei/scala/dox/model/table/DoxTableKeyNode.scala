package com.github.rehei.scala.dox.model.table

import scala.collection.Seq

object DoxTableKeyNode {
  val NONE = DoxTableKeyNode(DoxTableKeyNodeType.NONE, DoxTableKeyConfigExtended.NONE, Seq.empty)
}

case class DoxTableKeyNode(nodeType: DoxTableKeyNodeType, config: DoxTableKeyConfigExtended, children: Seq[DoxTableKeyNode]) {
  def setNodeType(newNodeType: DoxTableKeyNodeType) = {
    this.copy(nodeType = newNodeType)
  }
  def valueOf(index: Int, element: AnyRef) = {
    nodeType.valueOf(index, element)
  }

  def depth(): Int = {
    if (isLeaf()) { 0 } else { children.map(_.depth()).max + 1 }
  }

  def width(): Int = {
    if (isLeaf()) { 1 } else { leavesRecursive().size }
  }

  def isLeaf() = {
    children.isEmpty
  }

  def leavesRecursive(): Seq[DoxTableKeyNode] = {
    children.flatMap {
      child =>
        {
          if (child.isLeaf()) {
            Seq(child)
          } else {
            child.leavesRecursive()
          }
        }
    }
  }

  protected def leaves() = {
    children.filter(_.isLeaf())
  }

}

