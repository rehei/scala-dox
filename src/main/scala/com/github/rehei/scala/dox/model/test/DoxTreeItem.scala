package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Queue
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.TextAST

abstract class DoxTreeItem(val baseLabel: TextAST) {

  def isLeaf() = {
    this match {
      case leaf @ DoxLeaf(_, _) => true
      case node @ DoxNode(_) => false
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def nodeChildren() = {
    this match {
      case leaf @ DoxLeaf(_, _) => throw new Exception("Leaves do not have children")
      case node @ DoxNode(_) => node.children
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def leafProperty() = {
    this match {
      case leaf @ DoxLeaf(_, _) => leaf.property
      case node @ DoxNode(_) => throw new Exception("Nodes do not have values")
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def leaves() = {
    this match {
      case leaf @ DoxLeaf(_, _) => Seq(leaf)
      case node @ DoxNode(_) => node.leafChildren()
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }
}

