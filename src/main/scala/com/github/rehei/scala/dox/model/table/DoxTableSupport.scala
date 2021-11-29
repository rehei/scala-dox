package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSupport(root: DoxTableKeyNode) {

  val title = {
    root.children.headOption.map(head => head.nodeType match {
      case DoxTableKeyNodeType.TITLE => head.config.base.text
      case other                     => TextFactory.NONE
    }).getOrElse(TextFactory.NONE)
  }

  val noneTitleChildren = {
    removeObsoleteColumnSpace(
      root.children.headOption.map(
        head => head.nodeType match {
          case DoxTableKeyNodeType.TITLE => root.children.drop(1)
          case other                     => root.children
        }).getOrElse(root.children))

  }
  def checkValidity() {
    for (child <- noneTitleChildren) {
      findInvalid(child)
    }
  }

  def getChildrenSpaces() = {
    root.children.length match {
      case x if (x <= 1) => root.children
      case other         => spacedColumns()
    }
  }

  def removeObsoleteColumnSpace(parentChildren: Seq[DoxTableKeyNode]): Seq[DoxTableKeyNode] = {
    parentChildren.length match {
      case x if (x == 0) => Seq()
      case y if (y == 1) => getLastChild(parentChildren.head)
      case z if (z > 1) => {
        val cleaned = cleanSides(parentChildren)
        getCleanedChildren(cleaned) ++ getLastChild(cleaned.last)
      }
    }
  }

  protected def findInvalid(node: DoxTableKeyNode): Unit = {
    checkNode(node)
    for (child <- node.children) {
      findInvalid(child)
    }
  }

  protected def checkNode(node: DoxTableKeyNode) = {
    node.nodeType match {
      case DoxTableKeyNodeType.TITLE => throw new IllegalArgumentException("Title Node found, but not as first root node child.")
      case other                     => true
    }
  }

  protected def spacedColumns() = {
    val factory = DoxTableKeyNodeFactory()
    root.children.sliding(2).flatMap(children => Seq(children.head, factory.Columnspace())).toSeq ++ Seq(root.children.last)
  }

  protected def cleanSides(children: Seq[DoxTableKeyNode]) = {
    children.dropWhile(_.nodeType == DoxTableKeyNodeType.COLUMNSPACE).reverse.dropWhile(_.nodeType == DoxTableKeyNodeType.COLUMNSPACE).reverse
  }

  protected def getCleanedChildren(parentChildren: Seq[DoxTableKeyNode]) = {
    (parentChildren.sliding(2).map { case Seq(first, second) => first.copy(children = removeObsoleteColumnSpace(first.children)) }).toSeq
  }

  protected def getLastChild(child: DoxTableKeyNode) = {
    (child.nodeType match {
      case DoxTableKeyNodeType.COLUMNSPACE => Seq()
      case other                           => Seq(child.copy(children = removeObsoleteColumnSpace(child.children)))
    }).toSeq
  }
}