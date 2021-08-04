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

case class DoxTableNew[T <: AnyRef](treeTable: DoxRootNode)(implicit clazzTag: ClassTag[T]) {

  val data = ListBuffer[Seq[String]]()

  protected val query = new Query[T]()
  
  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    val elementApi = new QReflection(element)
    val rowValues = {
      for (leaf <- treeTable.endpointsSeq()) yield {
        leaf match {
          case leaf: DoxLeaf       => leaf.config.rendering.render(elementApi.get(leaf.propertyQuery))
          case index: DoxIndexNode => (data.length + 1).toString()
        }
      }
    }
    data.append(rowValues)
  }

  def caption = treeTable.rootConfig.caption

  def head = treeTable.treeRowsSeq()

}