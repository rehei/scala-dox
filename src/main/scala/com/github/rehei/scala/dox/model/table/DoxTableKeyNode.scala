package com.github.rehei.scala.dox.model.table

import scala.collection.Seq

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

case class DoxTableKeyNode(
  val textHeadOption:         Option[TextAST],
  val textBodyStrategyOption: Option[DoxTableKeyNodeValueStrategy],
  val format:                 DoxTableKeyNodeFormat,
  val children:               Seq[DoxTableKeyNode]) {

  def dimension(): DoxTableKeyNodeDimension = {
    val width = {
      textBodyStrategyOption.map { _.givenWidth } getOrElse { this.children.map(_.dimension().width).sum }
    }

    DoxTableKeyNodeDimension(width)
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

  def treeDepth(): Int = {
    if (isLeaf()) { 0 } else { children.map(_.treeDepth()).max + 1 }
  }

  def treeBreadth(): Int = {
    if (isLeaf()) { 1 } else { leavesRecursive().size }
  }

  def treeWidth(): Double = {
    if (isLeaf()) { dimension().width } else { leavesRecursive().map(_.dimension().width).sum }
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

