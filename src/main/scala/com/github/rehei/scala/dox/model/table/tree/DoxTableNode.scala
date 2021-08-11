package com.github.rehei.scala.dox.model.table.tree

import scala.collection.Seq

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxTableNode(nodeType: DoxTableNodeType, config: DoxTableKeyConfig, children: Seq[DoxTableNode]) {

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

  def leavesRecursive(): Seq[DoxTableNode] = {
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

