package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSupport(root: DoxTableKeyNode) {

  def noneTitleChildren = {
    filterNotTitles(root.children)
  }

  def addChildrenSpaces() = {
    root.children.length match {
      case x if (x < 1) => Seq.empty
      case other        => spacedColumns(root.children)
    }
  }

  protected def spacedColumns(children: Seq[DoxTableKeyNode]): Seq[DoxTableKeyNode] = {
    val children1 = {
      children
        .sliding(2)
        .flatMap({
          case Seq(onlyChild) => {
            Seq(processLastNode(onlyChild))
          }
          case Seq(firstChild, _) => processCurrentNode(firstChild)
        }).toSeq
    }
    val children2 = {
      if ((children.length >= 2)) {
        Seq(processLastNode(children.last))
      } else {
        Seq.empty
      }
    }
    children1 ++ children2
  }

  protected def processCurrentNode(node: DoxTableKeyNode) = {
    if (node.nodeType == DoxTableKeyNodeType.TITLE) {
      Seq(node.copy(children = spacedColumns(node.children)))
    } else {
      applyColumnSpace(node)
    }
  }

  protected def processLastNode(node: DoxTableKeyNode) = {
    if (node.nodeType == DoxTableKeyNodeType.TITLE) {
      node.copy(children = spacedColumns(node.children))
    } else {
      node
    }
  }

  protected def applyColumnSpace(node: DoxTableKeyNode) = {
    val factory = DoxTableKeyNodeFactory()
    if (node.children.length > 0) {
      Seq(node, factory.Columnspace())
    } else {
      Seq(node)
    }
  }
  //  def addChildrenSpaces() = {
  //    root.children.length match {
  //      case x if (x < 1)  => root.children
  //      case y if (y == 1) => {
  //        root.children.headOption.map(
  //            head => {
  //              if(head.nodeType == DoxTableKeyNodeType.TITLE){
  //
  //              }
  //                }
  //            )
  //      }
  //      case other         => spacedColumns()
  //    }
  //  }
  def removeChildrenObsoleteSpaces() = {
    removeObsoleteColumnSpace(root.children)
  }

  protected def filterNotTitles(children: Seq[DoxTableKeyNode]): Seq[DoxTableKeyNode] = {
    children
      .flatMap(child => {
        child.nodeType match {
          case DoxTableKeyNodeType.TITLE => filterNotTitles(child.children)
          case other                     => Seq(child)
        }
      })
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
    (parentChildren.sliding(2).map { case Seq(first, second) => first.copy(children = removeObsoleteColumnSpace(first.children)) }).toSeq
  }

  protected def getLastChild(child: DoxTableKeyNode) = {
    (child.nodeType match {
      case DoxTableKeyNodeType.COLUMNSPACE => Seq()
      case other                           => Seq(child.copy(children = removeObsoleteColumnSpace(child.children)))
    }).toSeq
  }

}