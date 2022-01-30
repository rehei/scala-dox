package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ArrayBuffer
import scala.reflect._
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import org.hamcrest.core.IsEqual

object DoxTable {
  val NONE = new DoxTable(DoxTableKeyNode.NONE)
}

case class DoxTable[T <: AnyRef](val root: DoxTableKeyNode)(implicit clazzTag: ClassTag[T]) {
  object DoxOptionBuilder {

    def value[A <: AnyRef, X <: AnyRef](callback1: A => X) = new {
      def rule[X <: AnyRef](callback2: () => X) = new {
        def space[X <: AnyRef](callback3: () => X) = new {
          DoxOptionBuilder(callback1, callback2, callback3)
        }
      }
    }
  }
  case class DoxOptionBuilder[A <: AnyRef, X <: AnyRef](valueCallback: A => X, ruleCallback: () => X, spaceCallback: () => X)

  sealed case class DoxValue[A <: AnyRef](value: A) extends DoxOption[A]
  case object DoxSpace extends DoxOption[Nothing]
  case object DoxRule extends DoxOption[Nothing]

  sealed abstract class DoxOption[+A] extends Product with Serializable {

    def render[X <: AnyRef](valueCallback: A => X, spaceCallback: () => X, ruleCallback: () => X) = {
      this match {
        case DoxValue(value) => valueCallback(value)
        case DoxRule         => ruleCallback()
        case DoxSpace        => spaceCallback()
      }
    }
    //    def render2[X <: AnyRef](callbacks: DoxOptionBuilder[A, X] => DoxOptionBuilder[A, X]) = {
    //
    //      this match {
    //        case DoxValue(value) => callbacks(DoxOptionBuilder[A, X]()).valueCallback(value)
    //        case DoxRule         => callbacks(DoxOptionBuilder[A, X]()).ruleCallback()
    //        case DoxSpace        => callbacks(DoxOptionBuilder[A, X]()).spaceCallback()
    //      }
    //    }

  }

  protected val content = ArrayBuffer[DoxOption[T]]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    content.append(DoxValue(element))
  }

  def addSpace() = {
    content.append(DoxSpace)
  }

  def addRule() = {
    content.append(DoxRule)
  }

  def addWithIntermediateSpacing(elementSeq: Iterable[T]) = {
    addWithIntermediateCallback(elementSeq, _.addSpace())
  }

  def addWithIntermediateRule(elementSeq: Iterable[T]) = {
    addWithIntermediateCallback(elementSeq, _.addRule())
  }

  def data() = {
    content
  }

  def head() = {
    new DoxTableHeadRepository(root).list()
  }

  def transform() = {
    new DoxTableMatrix(this)
  }

  protected def addWithIntermediateCallback(elementSeq: Iterable[T], callback: this.type => Unit) = {
    for ((element, index) <- elementSeq.zipWithIndex) {
      if (index == 0) {
        this.add(element)
      } else {
        callback(this)
        this.add(element)
      }
    }
  }

}