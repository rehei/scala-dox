package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

case class DoxTableFactory[T <: AnyRef](
    callbackConfig: DoxTableConfigBuilder.type => DoxTableConfig, 
    callbackSeq: (DoxTableFactoryKeySelection[T] => DoxTableFactoryKey)*)(implicit clazzTag: ClassTag[T]) {

  protected val query = new Query[T]()

  protected val keys = {
    callbackSeq.map(callback => callback(DoxTableFactoryKeySelection(query)))
  }

  protected val config = callbackConfig(DoxTableConfigBuilder)
  
  val head = keys.map(_.config)
  val data = ListBuffer[Seq[String]]()

  def addAll(elementSeq: Seq[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    data.append(keys.map(key => key.getValueOf(element)))
  }

  def get() = {
    DoxTable(config, head, data)
  }

}