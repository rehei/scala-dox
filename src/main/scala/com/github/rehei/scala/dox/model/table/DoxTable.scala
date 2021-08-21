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
    for (element <- _data) yield {
      extract(element)
    }
  }

  def caption = {
    Text2TEX.generate(root.config.text)
  }

  def head = {
    new DoxTableHeadRepository(root)
  }

  protected def extract(element: T) = {
    for (node <- root.leavesRecursive()) yield {
      node.valueOf(0, element)
    }
  }

}