package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTable[T <: AnyRef](val root: DoxTableKeyNode)(implicit clazzTag: ClassTag[T]) {

  protected val _data = ArrayBuffer[T]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    _data.append(element)
  }

  def data() = {
    for ((element, index) <- _data.zipWithIndex) yield {
      extract(element, index + 1)
    }
  }

  def caption = {
    Text2TEX.generate(root.config.text)
  }

  def head = {
    new DoxTableHeadRepository(root)
  }

  def transposed = {
    new DoxTableTransposedRepository(root, data)
  }

  def removeColumnSpaces() = {
    this.copy(root = root.copy(children = removeObsoleteColumnSpace(root.children)))
  }

  private[table] def withColumnSpace = {
    this.copy(root = root.copy(children = getChildrenSpaces()))
  }

  protected def getChildrenSpaces() = {
    root.children.length match {
      case x if (x <= 1) => root.children
      case other         => spacedColumns()
    }
  }

  protected def spacedColumns() = {
    val factory = DoxTableKeyNodeFactory()
    root.children.sliding(2).flatMap(children => Seq(children.head, factory.Columnspace())).toSeq ++ Seq(root.children.last)
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

  protected def extract(element: T, index: Int) = {
    for (node <- root.leavesRecursive()) yield {
      node.valueOf(index, element)
    }
  }

}