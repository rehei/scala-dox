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

case class DoxTableFactory_test[T <: AnyRef](treeTable: DoxNode)(implicit clazzTag: ClassTag[T]) {

  protected val query = new Query[T]()

  val tableConfig = treeTable.rootConfig.map(m => m).getOrElse(throw new Exception("Missing Table Config"))

  val head = treeTable.treeRows()//.map(_.map(_.nodeConfig))
  val data = ListBuffer[Seq[String]]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    val value = new QReflection(element)
    data.append(treeTable.leafChildren().map(leaf => leaf.config.rendering.render(value.get(leaf.propertyQuery))))
  }

//  def get() = {
//    DoxTable_test(tableConfig, head, data)
//  }
}