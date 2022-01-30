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
      new DoxTableKeyNode(this.nodeType, this.config, this.children ++ additionalChildren, None) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.nodeType, this.config, this.children ++ additionalChildren, None) with Writeable
    }
  }

  object Table extends {
    def create(node: DoxTableKeyNode) = {
      DoxTable[T](node)
    }
  }

  object Root {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, configExt(DoxTableKeyConfig.name("Root").alignment(_.CENTER)), Seq.empty, None) with Writeable
    }
  }

  object Index {
    def apply() = {
      nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.name("").alignment(_.CENTER))
    }
    def apply(name: String) = {
      nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.name(name).alignment(_.CENTER))
    }
  }

  object Blank {
    def apply() = {
      node(DoxTableKeyNodeType.BLANK).config(DoxTableKeyConfig.name("").alignment(_.CENTER)).width(Some(WIDTH_MIN))
    }
    def apply(width: Double) = {
      node(DoxTableKeyNodeType.BLANK).config(DoxTableKeyConfig.name("").alignment(_.CENTER)).width(Some(width))
    }
    def apply(widthOption: Option[Double]) = {
      node(DoxTableKeyNodeType.BLANK).config(DoxTableKeyConfig.name("").alignment(_.CENTER)).width(widthOption)
    }
    def apply(_alignment: DoxTableAlignment.type => DoxTableAlignment) = {
      val config = DoxTableKeyConfig(TextFactory.NONE, _alignment(DoxTableAlignment))
      new DoxTableKeyNode(DoxTableKeyNodeType.BLANK, configExt(config), Seq.empty, None) with Writeable {
        def width(_width: Option[Double]) = new {
          def finalize(callback: Query[T] => Query[_]) = {
            val query = callback(new Query[T])
            DoxTableKeyNode(DoxTableKeyNodeType.BLANK, config.setCategoryWidth(_width), Seq.empty, Some(query))
          }
        }
      }
    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INTERMEDIATE, configExt(_config), Seq.empty, None) with Writeable {
        def width(_width: Option[Double]) = new {
          def finalize(callback: Query[T] => Query[_]) = {
            val query = callback(new Query[T])
            DoxTableKeyNode(DoxTableKeyNodeType.VALUE, config.setCategoryWidth(_width), Seq.empty, Some(query))
          }
        }
      }
    }
  }

  protected def nodeWritable(_nodeType: DoxTableKeyNodeType) = {
    new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended.NONE, Seq.empty, None) with Writeable {
      def config(_config: DoxTableKeyConfig) = new DoxTableKeyNode(nodeType, configExt(_config), children, None) with Writeable {
        def width(_width: Option[Double]) = {
          new DoxTableKeyNode(nodeType, config.setCategoryWidth(_width), children, None) with Writeable
        }
      }
    }
  }

  protected def node(_nodeType: DoxTableKeyNodeType) = {
    new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended.NONE, Seq.empty, None) {
      def config(_config: DoxTableKeyConfig) = new DoxTableKeyNode(nodeType, configExt(_config), children, None) {
        def width(_width: Option[Double]) = {
          new DoxTableKeyNode(nodeType, config.setCategoryWidth(_width), children, None)
        }
      }
    }
  }

  protected def configExt(base: DoxTableKeyConfig) = {
    DoxTableKeyConfigExtended(base, None)
  }

}