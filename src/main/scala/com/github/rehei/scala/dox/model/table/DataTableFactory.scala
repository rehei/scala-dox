package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

import DataTableFactory.Key
import DataTableFactory.Selection
import DataTableFactory.Config

object DataTableFactory {
  
  object Config {
    def caption(caption: String) = new {
      def indexing(enableIndexing: Boolean) = {
        DataTableConfig(caption, enableIndexing)
      }
    }
  }

  class Selection[T](query: Query[T]) {
    def take(property: Query[T] => Query[_]) = new {
      def config(config: DataTableKeyConfig) = {
        new Key(property(query), config)
      }
    }
  }

  class Config(val caption: String)
  
  class Key(val query: Query[_], val config: DataTableKeyConfig) {
    def getValueOf(model: AnyRef) = {
      val value = new QReflection(model).get(query)
      config.rendering.render(value)
    }
  }

}

case class DataTableFactory[T <: AnyRef](
    callbackConfig: Config.type => DataTableConfig, 
    callbackSeq: (Selection[T] => Key)*)(implicit clazzTag: ClassTag[T]) {

  protected val query = new Query[T]()

  protected val keys = {
    callbackSeq.map(callback => callback(new Selection(query)))
  }

  protected val config = callbackConfig(Config)
  
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
    DataTable(config, head, data)
  }

}