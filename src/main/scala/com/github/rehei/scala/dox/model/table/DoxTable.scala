package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTable[T <: AnyRef](val totalRoot: DoxTableKeyNode)(implicit clazzTag: ClassTag[T]) {

  protected val _data = ArrayBuffer[T]()
  protected val tableSupport = DoxTableSupport(totalRoot)
  val root = totalRoot.copy(children = tableSupport.noneTitleChildren)
  val title = tableSupport.title

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

  def normal = {
    new DoxTableHeadRepository(root)
  }

  def transposed = {
    new DoxTableTransposedRepository(root, data)
  }

  def removeObsoleteSpaces() = {
    this.copy(totalRoot = totalRoot.copy(children = tableSupport.removeObsoleteColumnSpace(totalRoot.children)))
  }

  def withColumnSpace = {
    this.copy(totalRoot = totalRoot.copy(children = tableSupport.getChildrenSpaces()))
  }

  protected def extract(element: T, index: Int) = {
    for (node <- root.leavesRecursive()) yield {
      node.valueOf(index, element)
    }
  }

}