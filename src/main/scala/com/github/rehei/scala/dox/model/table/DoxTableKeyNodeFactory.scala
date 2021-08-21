package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

  trait Writeable extends DoxTableKeyNode {

    def append(additionalChildren: DoxTableKeyNode*) = {
      this.copy(children = children ++ additionalChildren)
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      this.copy(children = children ++ additionalChildren)
    }

  }

  object Table {
    def apply(node: DoxTableKeyNode) = {
      DoxTable[T](node)
    }
  }

  object Root {
    def apply(name: String) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable
    }
  }

  object Whitespace {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.WHITESPACE, DoxTableKeyConfig.NONE, Seq.empty)
    }
  }

  object Index {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INDEX, DoxTableKeyConfig.NONE, Seq.empty) with Writeable
    }
  }

  object Node {
    def apply(config: DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INTERMEDIATE, config, Seq.empty) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(DoxTableKeyNodeType.key(query), config, Seq.empty)
        }
      }
    }
  }

}