package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

  protected val WIDTH_MIN = 0.001

  trait Writeable extends DoxTableKeyNode {
    def append(additionalChildren: DoxTableKeyNode*) = {
      new DoxTableKeyNode(this.nodeType, this.config, this.children ++ additionalChildren) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.nodeType, this.config, this.children ++ additionalChildren) with Writeable
    }
  }

  object Table extends {
    def create(node: DoxTableKeyNode) = {
      DoxTable[T](node).withColumnSpace
    }
  }

  object Root {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, configExt(DoxTableKeyConfig.NONE.name("Root")), Seq.empty) with Writeable
    }
  }

  object Whitespace {
    def apply() = {
      node(DoxTableKeyNodeType.WHITESPACE).config(DoxTableKeyConfig.NONE).width(Some(WIDTH_MIN))
    }
    def apply(columnSize: Option[Double]) = {
      node(DoxTableKeyNodeType.WHITESPACE).config(DoxTableKeyConfig.NONE).width(Some(columnSize.getOrElse(WIDTH_MIN)))
    }
  }

  object Columnspace {
    def apply() = {
      node(DoxTableKeyNodeType.COLUMNSPACE).config(DoxTableKeyConfig.NONE).width(Some(WIDTH_MIN))
    }
    def apply(width: Double) = {
      node(DoxTableKeyNodeType.COLUMNSPACE).config(DoxTableKeyConfig.NONE).width(Some(width))
    }
  }

  object Index {
    def apply() = new {
      def width(_width: Option[Double]) = {
        nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.NONE.copy(alignment = DoxTableAlignment.CENTER)).width(_width)
      }
    }
    def apply(name: String) = new {
      def width(_width: Option[Double]) = {
        nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.NONE.name(name).copy(alignment = DoxTableAlignment.CENTER)).width(_width)
      }
    }
  }

  object Blank {
    def apply(_alignment: DoxTableAlignment.type => DoxTableAlignment) = {
      val config = DoxTableKeyConfig(TextFactory.NONE,_alignment(DoxTableAlignment))
      new DoxTableKeyNode(DoxTableKeyNodeType.BLANK, configExt(config), Seq.empty) with Writeable {
        def width(_width: Option[Double]) = new {
          def finalize(callback: Query[T] => Query[_]) = {
            val query = callback(new Query[T])
            DoxTableKeyNode(DoxTableKeyNodeType.key(query), config.setCategoryWidth(_width), Seq.empty)
          }
        }
      }
    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INTERMEDIATE, configExt(_config), Seq.empty) with Writeable {
        def width(_width: Option[Double]) = new {
          def finalize(callback: Query[T] => Query[_]) = {
            val query = callback(new Query[T])
            DoxTableKeyNode(DoxTableKeyNodeType.key(query), config.setCategoryWidth(_width), Seq.empty)
          }
        }
      }
    }
  }

  protected def nodeWritable(_nodeType: DoxTableKeyNodeType) = {
    new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended.NONE, Seq.empty) with Writeable {
      def config(_config: DoxTableKeyConfig) = new DoxTableKeyNode(nodeType, configExt(_config), children) with Writeable {
        def width(_width: Option[Double]) = {
          new DoxTableKeyNode(nodeType, config.setCategoryWidth(_width), children) with Writeable
        }
      }
    }
  }

  protected def node(_nodeType: DoxTableKeyNodeType) = {
    new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended.NONE, Seq.empty) {
      def config(_config: DoxTableKeyConfig) = new DoxTableKeyNode(nodeType, configExt(_config), children) {
        def width(_width: Option[Double]) = {
          new DoxTableKeyNode(nodeType, config.setCategoryWidth(_width), children)
        }
      }
    }
  }

  protected def configExt(base: DoxTableKeyConfig) = {
    DoxTableKeyConfigExtended(base, None)
  }

}