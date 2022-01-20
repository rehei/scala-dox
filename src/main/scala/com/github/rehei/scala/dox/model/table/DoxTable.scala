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

  sealed case class DoxValue[+A](value: A) extends DoxOption[A]
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
  }

  protected val _data = ArrayBuffer[DoxOption[T]]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    _data.append(DoxValue(element))
  }

  def addSpace() = {
    _data.append(DoxSpace)
  }

  def addRule() = {
    _data.append(DoxRule)
  }

  def addWithIntermediateSpacing(elementSeq: Iterable[T]) = {
    addWithIntermediateCallback(elementSeq, _.addSpace())
  }

  def addWithIntermediateRule(elementSeq: Iterable[T]) = {
    addWithIntermediateCallback(elementSeq, _.addRule())
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

  def data() = {
    for ((element, index) <- _data.zipWithIndex) yield {
      element match {
        case DoxValue(content) => DoxValue(extract(content, index + 1))
        case DoxSpace          => DoxSpace
        case DoxRule           => DoxRule
      }
    }
  }

  def caption = {
    Text2TEX(false).generate(root.config.base.text)
  }

  def head = {
    new DoxTableHeadRepository(root)
  }

  def withColumnSpace = {
    this.copy(root = root.addSpaces())
  }

  protected def extract(element: T, index: Int) = {
    for (node <- root.leavesRecursive()) yield {
      node.valueOf(index, element)
    }
  }

}