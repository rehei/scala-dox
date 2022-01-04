package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSupport(root: DoxTableKeyNode) {

  def addChildrenSpaces(node: DoxTableKeyNode) = {
    spacedColumns(node.children)
  }

  protected def spacedColumns(children: Seq[DoxTableKeyNode]): Seq[DoxTableKeyNode] = {
    if (children.length > 1) {
      applyToAllExceptLast(children) ++ children.lastOption.map(last => Seq(last.addSpaces())).getOrElse(Seq())
    } else {
      applyToAllExceptLast(children)
    }
  }

  protected def applyToAllExceptLast(children: Seq[DoxTableKeyNode]) = {
    children
      .sliding(2)
      .flatMap({
        case Seq(firstChild, _) => applyColumnSpace(firstChild)
        case Seq(onlyChild)     => Seq(onlyChild.addSpaces())
        case other              => other
      }).toSeq
  }

  protected def applyColumnSpace(node: DoxTableKeyNode) = {
    val factory = DoxTableKeyNodeFactory()
    if (!node.isLeaf()) {
      Seq(node.addSpaces(), factory.Columnspace())
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