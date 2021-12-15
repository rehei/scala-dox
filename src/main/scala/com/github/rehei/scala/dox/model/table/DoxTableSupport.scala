package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSupport(root: DoxTableKeyNode) {

  def addChildrenSpaces() = {
    root.children.length match {
      case x if (x < 1) => Seq.empty
      case other        => spacedColumns(root.children)
    }
  }

  protected def spacedColumns(children: Seq[DoxTableKeyNode]): Seq[DoxTableKeyNode] = {
    applyToAllExceptLast(children) ++ children.lastOption.map(last => Seq(last)).getOrElse(Seq())
  }

  protected def applyToAllExceptLast(children: Seq[DoxTableKeyNode]) = {
    children
      .sliding(2)
      .flatMap({
        case Seq(firstChild, _) => applyColumnSpace(firstChild)
        case other              => other
      }).toSeq
  }

  protected def applyColumnSpace(node: DoxTableKeyNode) = {
    val factory = DoxTableKeyNodeFactory()
    if (node.children.length > 0) {
      Seq(node, factory.Columnspace())
    } else {
      Seq(node)
    }
  }

  def removeChildrenObsoleteSpaces() = {
    removeObsoleteColumnSpace(root.children)
  }

  protected def removeObsoleteColumnSpace(parentChildren: Seq[DoxTableKeyNode]): Seq[DoxTableKeyNode] = {
    parentChildren.length match {
      case x if (x == 0) => Seq()
      case y if (y == 1) => getLastChild(parentChildren.head)
      case z if (z > 1) => {
        val cleaned = cleanSides(parentChildren)
        getCleanedChildren(cleaned) ++ getLastChild(cleaned.last)
      }
    }
  }

  protected def cleanSides(children: Seq[DoxTableKeyNode]) = {
    children.dropWhile(_.nodeType == DoxTableKeyNodeType.COLUMNSPACE).reverse.dropWhile(_.nodeType == DoxTableKeyNodeType.COLUMNSPACE).reverse
  }

  protected def getCleanedChildren(parentChildren: Seq[DoxTableKeyNode]) = {
    (parentChildren.sliding(2).map {
      case Seq(first, _) => first.copy(children = removeObsoleteColumnSpace(first.children))
      case other         => DoxTableKeyNode.NONE
    }).filterNot(_.nodeType == DoxTableKeyNodeType.NONE).toSeq
  }

  protected def getLastChild(child: DoxTableKeyNode) = {
    (child.nodeType match {
      case DoxTableKeyNodeType.COLUMNSPACE => Seq()
      case other                           => Seq(child.copy(children = removeObsoleteColumnSpace(child.children)))
    }).toSeq
  }

}