package com.github.rehei.scala.dox.model.table.tree

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.dox.model.tree.DoxNode
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.text.util.Text2TEX

case class DoxTableTree[T <: AnyRef](val root: DoxNode)(implicit clazzTag: ClassTag[T]) {

  protected val _data = ArrayBuffer[T]()
  protected val _query = new Query[T]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    _data.append(element)
  }

  def data() = {
    for (element <- _data) yield {
      extract(element)
    }
  }

  def caption = {
    Text2TEX.generate(root.config.text)
  }

  def head = {
    new DoxTableTreeHeadRepository(root)
  }

  protected def extract(element: T) = {
    for (node <- root.leavesRecursive()) yield {
      node.config.rendering.render(node.valueOf(0, element))
    }
  }

}