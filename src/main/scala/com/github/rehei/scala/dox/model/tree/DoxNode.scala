package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import scala.collection.Seq
import scala.collection.mutable.ListBuffer

case class DoxNode(nodeType: DoxNodeType, config: DoxTableKeyConfig, children: Seq[DoxNode]) {

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

  def leavesRecursive(): Seq[DoxNode] = {
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

