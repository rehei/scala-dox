package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

case class DoxTableFactoryKey(val query: Query[_], val config: DoxTableKeyConfig) {
  def getValueOf(model: AnyRef) = {
    val value = new QReflection(model).get(query)
    config.rendering.render(value)
  }
}
