package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

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
      nodeWritable(DoxTableKeyNodeType.ROOT).config(_.name("Root").alignment(_.CENTER))
    }
  }

  object Index {
    def apply() = {
      node(DoxTableKeyNodeType.INDEX).config(_.name(TextFactory.NONE).alignment(_.CENTER))
    }
    def apply(name: String) = {
      node(DoxTableKeyNodeType.INDEX).config(_.name(name).alignment(_.CENTER))
    }
  }

  object Blank {
    def apply() = {
      node(DoxTableKeyNodeType.BLANK).config(_.name(TextFactory.NONE).alignment(_.CENTER)).width(None)
    }
    def apply(width: Double) = {
      node(DoxTableKeyNodeType.BLANK).config(_.name(TextFactory.NONE).alignment(_.CENTER)).width(Some(width))
    }
    def apply(widthOption: Option[Double]) = {
      node(DoxTableKeyNodeType.BLANK).config(_.name(TextFactory.NONE).alignment(_.CENTER)).width(widthOption)
    }
    def apply(_alignment: DoxTableAlignment.type => DoxTableAlignment) = {
      val config = DoxTableKeyConfig(TextFactory.NONE, _alignment(DoxTableAlignment))
      new DoxTableKeyNode(DoxTableKeyNodeType.BLANK,  DoxTableKeyConfigExtended(config, None), Seq.empty, None) with Writeable {
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
      new DoxTableKeyNode(DoxTableKeyNodeType.INTERMEDIATE, DoxTableKeyConfigExtended(_config, None), Seq.empty, None) with Writeable {
        def width(_width: Option[Double]) = new {
          def finalize(callback: Query[T] => Query[_]) = {
            val query = callback(new Query[T])
            DoxTableKeyNode(DoxTableKeyNodeType.VALUE, DoxTableKeyConfigExtended(_config, _width), Seq.empty, Some(query))
          }
        }
      }
    }
  }

  protected def nodeWritable(_nodeType: DoxTableKeyNodeType) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended(_config(DoxTableKeyConfig), None), Seq.empty, None) with Writeable
    }
  }

  protected def node(_nodeType: DoxTableKeyNodeType) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = new {
      def width(_width: Option[Double]) = {
        new DoxTableKeyNode(_nodeType, DoxTableKeyConfigExtended(_config(DoxTableKeyConfig), _width), Seq.empty, None)
      }
    }
  }

}