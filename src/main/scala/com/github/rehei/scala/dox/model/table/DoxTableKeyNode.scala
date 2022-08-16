package com.github.rehei.scala.dox.model.table

import scala.collection.Seq

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

case class DoxTableKeyNode(val strategy: DoxTableKeyNodeValueStrategy, config: DoxTableKeyConfig, children: Seq[DoxTableKeyNode]) {

  def valueOf(index: Int, element: AnyRef) = {
    strategy.valueOf(index, element)
  }

  def hasNonEmptyChildren() = {
    children.filter(_.strategy.isNotBlank()).size > 0
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

  protected def leavesRecursive(): Seq[DoxTableKeyNode] = {
    childrenRecursive.filter(_.isLeaf())
  }

  def childrenRecursive(): Seq[DoxTableKeyNode] = {
    children.flatMap {
      child => Seq(child) ++ child.childrenRecursive()
    }
  }

  protected def leaves() = {
    children.filter(_.isLeaf())
  }

  protected def queryReflection(element: AnyRef, query: Query[_]) = {
    val value = new QReflection(element).get(query)
    value match {
      case m: TextAST => m
      case m          => TextFactory.text(m.toString())
    }
  }

}

