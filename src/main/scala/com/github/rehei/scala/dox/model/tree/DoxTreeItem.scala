package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import scala.collection.Seq

abstract class DoxTreeItem(val baseLabel: TextAST, val nodeConfig: DoxTableKeyConfig) {

  //children verwalten
  //doxrootnode
  //doxplaceholder
  
  def isLeaf() = {
    this match {
      case leaf @ DoxLeaf(_, _) => true
      case node @ DoxNode(_, _) => false
    }
  }

  def nodeChildren() = {
    this match {
      case leaf @ DoxLeaf(_, _) => Seq()
      case node @ DoxNode(_, _) => node.children
    }
  }

  def leafProperty() = {
    this match {
      case leaf @ DoxLeaf(_, _) => leaf.propertyQuery
    }
  }

  def leaves() = {
    this match {
      case leaf @ DoxLeaf(_, _) => Seq(leaf)
      case node @ DoxNode(_, _) => node.leafChildrenSeq()
    }
  }
}

