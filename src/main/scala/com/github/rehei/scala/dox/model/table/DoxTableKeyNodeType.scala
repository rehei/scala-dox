package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

object DoxTableKeyNodeType {

  object INTERMEDIATE extends DoxTableKeyNodeType
  object ROOT extends DoxTableKeyNodeType
  object WHITESPACE extends DoxTableKeyNodeType
  object RULE extends DoxTableKeyNodeType

  object INDEX extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.text(index.toString())
    }
  }

  def key(query: Query[_]) = {
    new DoxTableKeyNodeType() {
      override def valueOf(index: Int, element: AnyRef) = {
        val value = new QReflection(element).get(query).toString()
        TextFactory.text(value)
      }
    }
  }

  def keyMarkup(query: Query[TextAST]) = {
    new DoxTableKeyNodeType() {
      override def valueOf(index: Int, element: AnyRef) = {
        val value = new QReflection(element).get(query)
        value.asInstanceOf[TextAST]
      }
    }
  }

}

abstract class DoxTableKeyNodeType {

  def valueOf(index: Int, element: AnyRef): TextAST = {
    throw new UnsupportedOperationException()
  }

}