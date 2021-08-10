package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection

object MyDoxNodeType {

  object INTERMEDIATE extends MyDoxNodeType
  object ROOT extends MyDoxNodeType
  object EMPTY extends MyDoxNodeType
  
  object INDEX extends MyDoxNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      index.toString()
    }
  }

  def key(query: Query[_]) = {
    new MyDoxNodeType() {
      override def valueOf(index: Int, element: AnyRef) = {
        new QReflection(element).get(query)
      }
    }
  }

}

abstract class MyDoxNodeType {

  def valueOf(index: Int, element: AnyRef): AnyRef = {
    throw new UnsupportedOperationException()
  }

}