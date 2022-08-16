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

  protected object INTERMEDIATE extends DoxTableKeyNodeValueStrategy

  protected object ROOT extends DoxTableKeyNodeValueStrategy

  protected object BLANK extends DoxTableKeyNodeValueStrategy {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.NONE
    }
  }

  protected object INDEX extends DoxTableKeyNodeValueStrategy {
    override def valueOf(index: Int, element: AnyRef) = {
      TextFactory.text(index.toString())
    }
  }

  protected case class BySequenceIndex(sequenceIndex: Int) extends DoxTableKeyNodeValueStrategy {
    override def valueOf(index: Int, element: AnyRef) = {
      val sequence = element.asInstanceOf[Seq[_ <: AnyRef]]
      val value = sequence(sequenceIndex)
      convertAnyRef(value)
    }
  }

  protected case class ByQuery(query: Query[_]) extends DoxTableKeyNodeValueStrategy {
    override def valueOf(index: Int, element: AnyRef) = {
      val value = new QReflection(element).get(query)
      convertAnyRef(value)
    }
  }

  def intermediate() = {
    INTERMEDIATE
  }

  def root() = {
    ROOT
  }

  def blank() = {
    BLANK
  }

  def byRowIndex() = {
    INDEX
  }

  def bySequenceIndex(index: Int) = {
    BySequenceIndex(index)
  }

  def byValueQuery(query: Query[_]) = {
    ByQuery(query)
  }

}

abstract class DoxTableKeyNodeValueStrategy {

  def valueOf(index: Int, element: AnyRef): TextAST = {
    throw new UnsupportedOperationException()
  }

  def isNotBlank() = {
    !isBlank()
  }

  def isNotIntermediate() = {
    !isIntermediate()
  }

  def isBlank() = {
    this == DoxTableKeyNodeValueStrategy.BLANK
  }

  def isIntermediate() = {
    this == DoxTableKeyNodeValueStrategy.INTERMEDIATE
  }

  protected def convertAnyRef(value: AnyRef) = {
    value match {
      case m: TextAST => m
      case m          => TextFactory.text(m.toString())
    }
  }

}