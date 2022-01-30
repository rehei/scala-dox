package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.macros.util.QReflection

object DoxTableKeyNode {
  val NONE = DoxTableKeyNode(DoxTableKeyNodeType.NONE, DoxTableKeyConfigExtended.NONE, Seq.empty, None)
}

case class DoxTableKeyNode(
  nodeType:    DoxTableKeyNodeType,
  config:      DoxTableKeyConfigExtended,
  children:    Seq[DoxTableKeyNode],
  queryOption: Option[Query[_]]) {

  protected val tableSupport = DoxTableSupport(this)

  def addSpaces() = {
    this.copy(children = tableSupport.addChildrenSpaces(this))
  }

  def setNodeType(newNodeType: DoxTableKeyNodeType) = {
    this.copy(nodeType = newNodeType)
  }

  def valueOf(index: Int, element: AnyRef) = {
    queryOption
      .map(query => queryReflection(element, query))
      .getOrElse(nodeType.valueOf(index, element))
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

  protected def queryReflection(element: AnyRef, query: Query[_]) = {
    val value = new QReflection(element).get(query)
    value match {
      case m: TextAST => m
      case m          => TextFactory.text(m.toString())
    }
  }

}

