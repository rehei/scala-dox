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

  object TITLE extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object INDEX extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.text(index.toString())
    }
  }

  object COLUMNSPACE extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }

  def key(query: Query[_]) = {
    new DoxTableKeyNodeType() {
      override def valueOf(index: Int, element: AnyRef) = {
        val value = new QReflection(element).get(query)

        value match {
          case m: TextAST => m
          case m          => TextFactory.text(m.toString())
        }
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