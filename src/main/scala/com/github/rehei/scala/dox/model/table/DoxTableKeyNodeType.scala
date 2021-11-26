package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

object DoxTableKeyNodeType {
  object NONE extends DoxTableKeyNodeType("NONE")
  object INTERMEDIATE extends DoxTableKeyNodeType("INTERMEDIATE") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object ROOT extends DoxTableKeyNodeType("ROOT")
  object WHITESPACE extends DoxTableKeyNodeType("WHITESPACE")
  object RULE extends DoxTableKeyNodeType("RULE")

  object TITLE extends DoxTableKeyNodeType("TITLE") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object INDEX extends DoxTableKeyNodeType("INDEX") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.text(index.toString())
    }
  }

  object COLUMNSPACE extends DoxTableKeyNodeType("COLUMNSPACE") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }

  def key(query: Query[_]) = {
    new DoxTableKeyNodeType("NONE") {
      override def valueOf(index: Int, element: AnyRef) = {
        val value = new QReflection(element).get(query)

        value match {
          case m: TextAST => m
          case m          => TextFactory.text(m.toString())
        }
      }
    }
  }
}

abstract class DoxTableKeyNodeType(typey: String) {

  def valueOf(index: Int, element: AnyRef): TextAST = {
    throw new UnsupportedOperationException(typey)
  }

}