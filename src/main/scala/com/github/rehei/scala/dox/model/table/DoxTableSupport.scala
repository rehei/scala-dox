package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSupport() {

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
      }).toSeq
  }

  protected def applyColumnSpace(node: DoxTableKeyNode) = {
    val factory = DoxTableKeyNodeFactory()
    if (!node.isLeaf()) {
      Seq(node.addSpaces(), factory.Blank())
    } else {
      Seq(node)
    }
  }

}