package com.github.rehei.scala.dox.model.test

import scala.collection.Seq
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableConfig
import com.github.rehei.scala.dox.model.table.DoxTableConfigBuilder
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.tree.DoxNode
import com.github.rehei.scala.dox.model.tree.DoxRootNode
import com.github.rehei.scala.dox.model.tree.DoxLeaf
import com.github.rehei.scala.dox.model.tree.DoxIndexNode
import scala.collection.mutable.ArrayBuffer

case class DoxTableNew[T <: AnyRef](protected val root: DoxRootNode)(implicit clazzTag: ClassTag[T]) {

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
    root.caption
  }

  def head = {
    root.treeRowsSeq()
  }
  
  def leaves() = {
    root.endpointsSeq()
  }

  protected def extract(element: T) = {
    val elementApi = new QReflection(element)

    for (leaf <- root.endpointsSeq()) yield {
      leaf match {
        case leaf: DoxLeaf       => leaf.config.rendering.render(elementApi.get(leaf.propertyQuery))
        case index: DoxIndexNode => (_data.length + 1).toString()
      }
    }
  }

}