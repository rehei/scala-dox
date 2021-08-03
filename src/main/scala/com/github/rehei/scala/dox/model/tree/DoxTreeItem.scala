package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import scala.collection.Seq
import scala.collection.mutable.ListBuffer

abstract class DoxTreeItem(val nodeConfig: DoxTableKeyConfig) {

  val children = ListBuffer[DoxTreeItem]()

  def isLeaf() = {
    this match {
      case leaf @ DoxLeaf(_, _)     => true
      case node @ DoxNode(_)        => false
      case node @ DoxPlaceholder(_) => false
    }
  }

  def leafPropertyQuery() = {
    this match {
      case leaf @ DoxLeaf(_, _) => leaf.propertyQuery
    }
  }

  def endPoints() = {
    this match {
      case leaf @ DoxLeaf(_, _)            => Seq(leaf)
      case node @ DoxNode(_)               => node.leafChildrenSeq()
      case placeholder @ DoxPlaceholder(_) => Seq(placeholder)
    }
  }
}

