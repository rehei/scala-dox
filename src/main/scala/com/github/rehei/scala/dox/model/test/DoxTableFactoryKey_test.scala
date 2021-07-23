package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

case class DoxTableFactoryKey_test(val query: Query[_], val config: DoxTableKeyConfig_test) {
  def getValueOf(model: AnyRef) = {
    val value = new QReflection(model).get(query)
    config.rendering.render(value)
  }
}
