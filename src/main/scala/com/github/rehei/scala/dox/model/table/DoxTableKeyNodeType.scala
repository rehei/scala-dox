package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import scala.runtime.Tuple3Zipped.Ops
import scala.Option
import java.util.Optional

object DoxTableKeyNodeType {
  object NONE extends DoxTableKeyNodeType("NONE")
  object INTERMEDIATE extends DoxTableKeyNodeType("INTERMEDIATE") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object ROOT extends DoxTableKeyNodeType("ROOT")

  object BLANK extends DoxTableKeyNodeType("BLANK") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object VALUE extends DoxTableKeyNodeType("VALUE") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
  object INDEX extends DoxTableKeyNodeType("INDEX") {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.text(index.toString())
    }
  }

}

abstract class DoxTableKeyNodeType(typey: String) {

  override def toString() = {
    typey
  }

  def valueOf(index: Int, element: AnyRef): TextAST = {
    throw new UnsupportedOperationException(typey)
  }

}