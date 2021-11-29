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
      val root = nodeWritable(DoxTableKeyNodeType.ROOT).config(DoxTableKeyConfig.NONE.name(name)).width(None)
      title.map {
        text => { root.append(Title(name)) }
      } getOrElse {
        root
      }
    }
  }

  object Whitespace {
    def apply() = {
      node(DoxTableKeyNodeType.WHITESPACE).config(DoxTableKeyConfig.NONE).width(Some(0.1))

    }
    def apply(columnSize: Double) = {
      node(DoxTableKeyNodeType.WHITESPACE).config(DoxTableKeyConfig.NONE).width(Some(columnSize))

    }
  }

  object Columnspace {
    def apply() = {
      node(DoxTableKeyNodeType.COLUMNSPACE).config(DoxTableKeyConfig.NONE).width(Some(0.1))

    }
  }

  object Title {
    def apply(name: String) = {
      node(DoxTableKeyNodeType.TITLE).config(DoxTableKeyConfig.NONE.name(name)).width(None)

    }
  }

  object Index {
    def apply() = {
      nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.NONE).width(None)
    }
    def apply(name: String) = {
      nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.NONE.name(name)).width(None)

    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INTERMEDIATE, configExt(_config), Seq.empty) with Writeable {
        def width(_width: Option[Double]) = new {
          def finalize(callback: Query[T] => Query[_]) = {
            val query = callback(new Query[T])
            DoxTableKeyNode(DoxTableKeyNodeType.key(query), config.setWidth(_width), Seq.empty)
          }
        }

      }
    }
  }

  protected def nodeWritable(_nodeType: DoxTableKeyNodeType) = {
    new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended.NONE, Seq.empty) with Writeable {
      def config(_config: DoxTableKeyConfig) = new DoxTableKeyNode(nodeType, configExt(_config), children) with Writeable {
        def width(_width: Option[Double]) = {
          new DoxTableKeyNode(nodeType, config.setWidth(_width), children) with Writeable
        }
      }
    }
  }
  protected def node(_nodeType: DoxTableKeyNodeType) = {
    new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended.NONE, Seq.empty) {
      def config(_config: DoxTableKeyConfig) = new DoxTableKeyNode(nodeType, configExt(_config), children) {
        def width(_width: Option[Double]) = {
          new DoxTableKeyNode(nodeType, config.setWidth(_width), children)
        }
      }
    }
  }
  protected def configExt(base: DoxTableKeyConfig) = {
    DoxTableKeyConfigExtended(base, None)
  }
}