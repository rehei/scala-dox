package com.github.rehei.scala.dox.model.table.tree

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

object DoxTableNodeType {

  object INTERMEDIATE extends DoxTableNodeType
  object ROOT extends DoxTableNodeType
  object WHITESPACE extends DoxTableNodeType
  object RULE extends DoxTableNodeType

  object INDEX extends DoxTableNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      index.toString()
    }
  }

  def key(query: Query[_]) = {
    new DoxTableNodeType() {
      override def valueOf(index: Int, element: AnyRef) = {
        new QReflection(element).get(query)
      }
    }
  }

}

abstract class DoxTableNodeType {

  def valueOf(index: Int, element: AnyRef): AnyRef = {
    throw new UnsupportedOperationException()
  }

}