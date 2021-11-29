package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

  trait Writeable extends DoxTableKeyNode {
    def append(additionalChildren: DoxTableKeyNode*) = {
      new DoxTableKeyNode(this.nodeType, this.config, children ++ additionalChildren) with Writeable
    }
    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.nodeType, this.config, children ++ additionalChildren) with Writeable
    }
  }

  object Table {
    def apply(node: DoxTableKeyNode) = {
      DoxTable[T](node).withColumnSpace
    }
  }

  object Root {
    def apply(name: String, title: Option[String]) = {
      title.map(text => {
        (new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable)
          .append(new DoxTableKeyNode(DoxTableKeyNodeType.TITLE, DoxTableKeyConfig.NONE.name(text), Seq.empty))
      }).getOrElse(
        new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable)
    }
  }

  object Whitespace {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.WHITESPACE, DoxTableKeyConfig.NONE, Seq.empty)
    }
    def apply(columnSize: Double) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.WHITESPACE, DoxTableKeyConfig.NONE.setSize(columnSize), Seq.empty)
    }
  }

  object Columnspace {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.COLUMNSPACE, DoxTableKeyConfig.NONE.setSize(0.1), Seq.empty)
    }
  }

  object Title {
    def apply(name: String) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.TITLE, DoxTableKeyConfig.NONE.name(name), Seq.empty)
    }
  }

  object Index {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INDEX, DoxTableKeyConfig.NONE, Seq.empty) with Writeable
    }
    def apply(name: String) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INDEX, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable
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