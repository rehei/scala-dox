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
  val NONE = new DoxTable(DoxTableKeyNode.NONE, None)

}

case class DoxTable[T <: AnyRef](val root: DoxTableKeyNode, headTitle: Option[DoxTableKeyConfigExtended])(implicit clazzTag: ClassTag[T]) {

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

  sealed case class DoxValue[+A](value: A) extends DoxOption[A] {
    def isEmpty = false
    def get = value
  }

  case object DoxSpace extends DoxOption[Nothing] {
    def isEmpty = false
    def get = throw new NoSuchElementException("None.get")
  }

  case object DoxRule extends DoxOption[Nothing] {
    def isEmpty = false
    def get = throw new NoSuchElementException("None.get")
  }
  sealed abstract class DoxOption[+A] extends Product with Serializable {
    def isEmpty: Boolean
    def get: A
    def render[X <: AnyRef](valueCallback: A => X, spaceCallback: () => X, ruleCallback: () => X) = {

      this match {
        case DoxValue(value) => valueCallback(value)
        case DoxRule         => ruleCallback()
        case DoxSpace        => spaceCallback()
      }
    }
    //    def render[X <: AnyRef](callbacks: DoxOptionBuilder.type => DoxOptionBuilder[A, X]) = {
    //
    //      this match {
    //        case DoxValue(value) => callbacks(DoxOptionBuilder).valueCallback(value)
    //        case DoxRule         => callbacks(DoxOptionBuilder).ruleCallback()
    //        case DoxSpace        => callbacks(DoxOptionBuilder).spaceCallback()
    //      }
    //    }

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

  def data() = {
    for ((element, index) <- _data.zipWithIndex) yield {
      element match {
        case x: DoxValue[T] => DoxValue(extract(x.get, index + 1))
        case DoxSpace       => DoxSpace
        case DoxRule        => DoxRule
      }
    }
  }

  def caption = {
    Text2TEX.generate(root.config.base.text)
  }

  def normal = {
    new DoxTableHeadRepository(root)
  }

  //  def transposed = {
  //    new DoxTableTransposedRepository(root, ArrayBuffer(Seq.empty) /* data*/ )
  //  }

  def withColumnSpace = {
    this.copy(root = root.addSpaces())
  }

  protected def extract(element: T, index: Int) = {
    for (node <- root.leavesRecursive()) yield {
      node.valueOf(index, element)
    }
  }

}