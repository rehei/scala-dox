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

  case class ByRowIndex(givenWidth: Double) extends DoxTableKeyNodeValueStrategy {

    override def width() = givenWidth

    override def valueOf(row: Int, element: AnyRef) = {
      TextFactory.text(row.toString())
    }
  }

  case class ByQueryAndSequenceIndex(givenWidth: Double, query: Query[_], sequenceIndex: Int) extends DoxTableKeyNodeValueStrategy {

    override def width() = givenWidth

    override def valueOf(row: Int, element: AnyRef) = {
      val sequence = new QReflection(element).get(query).asInstanceOf[Seq[_ <: AnyRef]]
      val value = sequence(sequenceIndex)
      convertAnyRef(value)
    }
  }

  case class BySequenceIndex(givenWidth: Double, sequenceIndex: Int) extends DoxTableKeyNodeValueStrategy {

    override def width() = givenWidth

    override def valueOf(row: Int, element: AnyRef) = {
      val sequence = element.asInstanceOf[Seq[_ <: AnyRef]]
      val value = sequence(sequenceIndex)
      convertAnyRef(value)
    }
  }

  case class ByQuery(givenWidth: Double, query: Query[_]) extends DoxTableKeyNodeValueStrategy {

    override def width() = givenWidth

    override def valueOf(row: Int, element: AnyRef) = {
      val value = new QReflection(element).get(query)
      convertAnyRef(value)
    }
  }

  case class Spacing(givenWidth: Double) extends DoxTableKeyNodeValueStrategy {

    override def width() = givenWidth

    override def valueOf(row: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }
}

abstract class DoxTableKeyNodeValueStrategy {

  def width(): Double

  def valueOf(row: Int, element: AnyRef): TextAST

  protected def convertAnyRef(value: AnyRef) = {
    value match {
      case m: TextAST => m
      case m          => TextFactory.text(m.toString())
    }
  }

}