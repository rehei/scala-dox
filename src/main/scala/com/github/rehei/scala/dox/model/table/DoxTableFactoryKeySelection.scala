package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query

case class DoxTableFactoryKeySelection[T](query: Query[T]) {
  def take(property: Query[T] => Query[_]) = new {
    def config(config: DoxTableKeyConfig) = {
      new DoxTableFactoryKey(property(query), config)
    }
  }
}