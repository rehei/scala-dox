package com.github.rehei.scala.dox.model.table

import scala.collection.Seq

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

case class DoxTableKeyNode(
  val textHeadOption:         Option[TextAST],
  val textBodyStrategyOption: Option[DoxTableKeyNodeValueStrategy],
  val alignment:              Option[DoxTableAlignment],
  val children:               Seq[DoxTableKeyNode]) {

  def format() = {
    DoxTableKeyNodeFormat(textBodyStrategyOption.map(_.width()).get, DoxTableAlignment.CENTER)
  }

  def valueOf(index: Int, element: AnyRef) = {
    textBodyStrategyOption.map(_.valueOf(index, element)).get
  }

  def hasAnyHeadDefinedChildren() = {
    children.filter(_.isHeadDefined).size > 0
  }

  def textHead() = {
    textHeadOption.getOrElse(TextFactory.NONE)
  }

  def isBodyDefined() = {
    textBodyStrategyOption.isDefined
  }

  def isHeadDefined() = {
    textHeadOption.isDefined
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

}

