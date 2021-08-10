package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

object DoxNodeType {

  object INTERMEDIATE extends DoxNodeType
  object ROOT extends DoxNodeType
  object WHITESPACE extends DoxNodeType
  object RULE extends DoxNodeType

  object INDEX extends DoxNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      index.toString()
    }
  }

  def key(query: Query[_]) = {
    new DoxNodeType() {
      override def valueOf(index: Int, element: AnyRef) = {
        new QReflection(element).get(query)
      }
    }
  }

}

abstract class DoxNodeType {

  def valueOf(index: Int, element: AnyRef): AnyRef = {
    throw new UnsupportedOperationException()
  }

}