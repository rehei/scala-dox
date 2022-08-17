package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory

case class TreeWithSpacingBetween() {

  case class SiblingLookahead(node: Option[DoxTableKeyNode], next: Option[DoxTableKeyNode])

  protected val factory = DoxTableKeyNodeFactory()

  def addSpaces(base: DoxTableKeyNode): DoxTableKeyNode = {

    val all = base.children.indices.map(index => toLookahead(base, index))

    val children = {
      {
        for (wrapper <- all) yield {
          wrapper match {
            case SiblingLookahead(Some(node), Some(next)) if node.isLeaf() => Seq(node, factory.Space())
            case SiblingLookahead(Some(node), Some(next))                  => Seq(addSpaces(node), factory.Space())
            case SiblingLookahead(Some(node), None) if (node.isLeaf())     => Seq(node)
            case SiblingLookahead(Some(node), None)                        => Seq(addSpaces(node))
          }
        }
      }
    }

    base.copy(children = children.flatten)
  }

  protected def toLookahead(base: DoxTableKeyNode, index: Int) = {

    val node = base.children.lift(index)
    val next = base.children.lift(index + 1)

    SiblingLookahead(node, next)
  }

}