package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.dox.model.tree.MyDoxNode
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.text.util.Text2TEX

case class DoxTableNew[T <: AnyRef](val root: MyDoxNode)(implicit clazzTag: ClassTag[T]) {

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

    val elementApi = new QReflection(element)

    for (node <- root.leavesRecursive()) yield {
      node.config.rendering.render(node.valueOf(0, element))
    }
  }

}