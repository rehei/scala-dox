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

case class DoxTableFactory_test[T <: AnyRef](treeTable: DoxNode)(implicit clazzTag: ClassTag[T]) {

  protected val query = new Query[T]()
  protected var index: Option[DoxTableKeyConfig] = None

  val tableConfig = treeTable.rootConfig.map(m => m).getOrElse(throw new Exception("Missing Table Config"))

  protected val _head = treeTable.treeRows()
  protected val _data = ListBuffer[Seq[String]]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    val value = new QReflection(element)
    _data.append(treeTable.leafChildren().map(leaf => leaf.config.rendering.render(value.get(leaf.propertyQuery))))
  }

  def withIndex(indexConfig: Option[DoxTableKeyConfig]) = {
    if (tableConfig.enableIndexing) {
      index = indexConfig
    }
  }

  def head = {
    if (index.isDefined) {
      val indexedHeadRow = _head.headOption.map(headRow => DoxLeaf(index.get, null) +: headRow).getOrElse(Seq[DoxTreeItem]())
      _head.drop(1).prepend(indexedHeadRow)
    }
    _head
  }

  def data = {
    if (index.isDefined) {
      _data.zipWithIndex.map { case (dataRow, index) => (index + 1).toString() +: dataRow }
    } else {
      _data
    }
  }

  protected def addIndex() = {
    if (tableConfig.enableIndexing) {
      _data.zipWithIndex.map { case (dataRow, index) => (index + 1).toString() +: dataRow }
    } else {

    }
  }
}