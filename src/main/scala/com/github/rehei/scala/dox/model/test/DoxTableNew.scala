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

case class DoxTableNew[T <: AnyRef](treeTable: DoxRootNode)(implicit clazzTag: ClassTag[T]) {

  protected val query = new Query[T]()
  protected val _data = ListBuffer[Seq[String]]()

  protected var index: Option[DoxTableKeyConfig] = None

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    val elementApi = new QReflection(element)
    _data.append(treeTable.leafChildrenSeq().map(leaf => leaf.config.rendering.render(elementApi.get(leaf.propertyQuery))))
  }

  def caption = treeTable.rootConfig.caption

  def withIndex(indexConfig: Option[DoxTableKeyConfig]) = {
    if (treeTable.rootConfig.enableIndexing) {
      index = indexConfig
    }
  }

  def head = treeTable.treeRowsSeq(index)

  def data = {
    if (index.isDefined) {
      _data.zipWithIndex.map { case (dataRow, index) => (index + 1).toString() +: dataRow }
    } else {
      _data
    }
  }
}