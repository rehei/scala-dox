package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.macros.util.QReflection
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import scala.runtime.Tuple3Zipped.Ops
import scala.Option
import java.util.Optional
import com.github.rehei.scala.dox.util.NextID

object DoxTableKeyNodeValueStrategy {

  case class ByRowIndex() extends DoxTableKeyNodeValueStrategy {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.text(index.toString())
    }
  }

  case class BySequenceIndex(sequenceIndex: Int) extends DoxTableKeyNodeValueStrategy {
    override def valueOf(index: Int, element: AnyRef) = {
      val sequence = element.asInstanceOf[Seq[_ <: AnyRef]]
      val value = sequence(sequenceIndex)
      convertAnyRef(value)
    }
  }

  case class ByQuery(query: Query[_]) extends DoxTableKeyNodeValueStrategy {
    override def valueOf(index: Int, element: AnyRef) = {
      val value = new QReflection(element).get(query)
      convertAnyRef(value)
    }
  }

}

abstract class DoxTableKeyNodeValueStrategy {

  def valueOf(index: Int, element: AnyRef): TextAST
  
  protected def convertAnyRef(value: AnyRef) = {
    value match {
      case m: TextAST => m
      case m          => TextFactory.text(m.toString())
    }
  }

}