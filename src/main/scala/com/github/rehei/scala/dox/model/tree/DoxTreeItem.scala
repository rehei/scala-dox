package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import scala.collection.Seq

abstract class DoxTreeItem(val baseLabel: TextAST, val nodeConfig: DoxTableKeyConfig) {

  def isLeaf() = {
    this match {
      case leaf @ DoxLeaf(_, _) => true
      case node @ DoxNode(_, _) => false
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def nodeChildren() = {
    this match {
      case leaf @ DoxLeaf(_, _) => throw new Exception("Leaves do not have children")
      case node @ DoxNode(_, _) => node.children
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def leafProperty() = {
    this match {
      case leaf @ DoxLeaf(_, _) => leaf.propertyQuery
      case node @ DoxNode(_, _) => throw new Exception("Nodes do not have values")
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }

  def leaves() = {
    this match {
      case leaf @ DoxLeaf(_, _) => Seq(leaf)
      case node @ DoxNode(_, _) => node.leafChildren()
      case _                    => throw new Exception("Neither Node nor Leaf")
    }
  }
}

