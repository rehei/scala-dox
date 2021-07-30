package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

case class DoxTreeBuilder[T](rootNode:DoxNode)(implicit clazzTag: ClassTag[T]) {
  protected val queryBase = new Query[T]()
//  
//  def addLeaf(config:DoxTableKeyConfig,property:Query[T] => Query[_]) = {
//    rootNode.addNodes(DoxLeaf(config,property))
//  }
}