package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import scala.runtime.Tuple3Zipped.Ops
import scala.Option
import java.util.Optional

object DoxTableKeyNodeType {
  
  object INTERMEDIATE extends DoxTableKeyNodeType
  
  object ROOT extends DoxTableKeyNodeType

  object BLANK extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object VALUE extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object INDEX extends DoxTableKeyNodeType {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.text(index.toString())
    }
  }

}

abstract class DoxTableKeyNodeType {

  def valueOf(index: Int, element: AnyRef): TextAST = {
    throw new UnsupportedOperationException()
  }

}