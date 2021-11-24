package com.github.rehei.scala.dox.model.table

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import com.github.rehei.scala.macros.Query
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTable[T <: AnyRef](val root: DoxTableKeyNode)(implicit clazzTag: ClassTag[T]) {

  protected val _data = ArrayBuffer[T]()

  def addAll(elementSeq: Iterable[T]) {
    for (element <- elementSeq) {
      add(element)
    }
  }

  def add(element: T) {
    _data.append(element)
  }

  def data() = {
    for ((element, index) <- _data.zipWithIndex) yield {
      extract(element, index + 1)
    }
  }

  def caption = {
    Text2TEX.generate(root.config.text)
  }

  def head = {
    new DoxTableHeadRepository(root)
  }

  def transposed = {
    new DoxTableTransposedRepository(root, data)
  }

  def withoutColumnSpace = {
    val filteredRoot = {
//      println(root.children.length)
      root.children.filterNot(childNode => {
        childNode.nodeType match {
          case m @ DoxTableKeyNodeType.COLUMNSPACE => true
          case m @ other                           => false
        }
      })
    }
    //    println("a")
    //    filteredRoot.map(asd => println(asd.nodeType))
    //    println("b")
    //    println("---")
    //    println(filteredRoot.length)
    filteredRoot.toList.map(node => println(node.children.length))
    //    println(root.children.length)
    root.children.toList.map(node => println(node.children.length))

    this.copy(root = root.copy(children = filteredRoot))
    this
  }

  private[table] def withColumnSpace = {
    val factory = DoxTableKeyNodeFactory()
    val childrenWithColumnspace = {
      if (root.children.length <= 1) {
        root.children
      } else {
        val columnSpaced = {
          (for (childrenSubgroup <- root.children.sliding(2)) yield {

            Seq(childrenSubgroup.head, factory.Columnspace())
          }).flatten.toSeq
        }
        columnSpaced ++ Seq(root.children.last)
      }
    }

    this.copy(root = root.copy(children = childrenWithColumnspace))
    this
  }

  protected def extract(element: T, index: Int) = {
    for (node <- root.leavesRecursive()) yield {
      node.valueOf(index, element)
    }
  }

}