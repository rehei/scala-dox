package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

object DoxTableKeyNodeType {

  object INTERMEDIATE extends DoxTableKeyNodeType
  object ROOT extends DoxTableKeyNodeType
  object WHITESPACE extends DoxTableKeyNodeType
  object RULE extends DoxTableKeyNodeType

  object INDEX extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      index.toString()
    }
  }

  def key(query: Query[_]) = {
    new DoxTableKeyNodeType() {
      override def valueOf(index: Int, element: AnyRef) = {
        new QReflection(element).get(query).toString()
      }
    }
  }

}

abstract class DoxTableKeyNodeType {

  def valueOf(index: Int, element: AnyRef): String = {
    throw new UnsupportedOperationException()
  }

}