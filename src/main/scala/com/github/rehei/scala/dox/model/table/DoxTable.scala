package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ArrayBuffer

case class DoxTable[T <: AnyRef](val root: DoxTableKeyNode) {

  import com.github.rehei.scala.dox.model.table.content.DoxContent._

  protected val content = ArrayBuffer[DoxOption[T]]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    content.append(DoxValue(element))
  }

  def addSpace() = {
    content.append(DoxSpace)
  }

  def addRule() = {
    content.append(DoxRule)
  }

  def addWithIntermediateSpacing(elementSeq: Iterable[T]) = {
    addWithIntermediateCallback(elementSeq, _.addSpace())
  }

  def addWithIntermediateRule(elementSeq: Iterable[T]) = {
    addWithIntermediateCallback(elementSeq, _.addRule())
  }

  def data() = {
    content
  }

  def head() = {
    new DoxTableHeadRepository(root).list()
  }

  def transform() = {
    new DoxTableMatrix(this)
  }

  protected def addWithIntermediateCallback(elementSeq: Iterable[T], callback: this.type => Unit) = {
    for ((element, index) <- elementSeq.zipWithIndex) {
      if (index == 0) {
        this.add(element)
      } else {
        callback(this)
        this.add(element)
      }
    }
  }

}