package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

  trait Writeable extends DoxTableKeyNode {

    def append(additionalChildren: DoxTableKeyNode*) = {
      new DoxTableKeyNode(this.nodeType, this.config, this.children ++ additionalChildren) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.nodeType, this.config, this.children ++ additionalChildren) with Writeable
    }
  }

  object Table extends {
    def title(headTitle: Option[DoxTableKeyConfig]) = new {
      def root(node: DoxTableKeyNode) = {
        DoxTable[T](node, headTitle.map(configExt)).withColumnSpace
      }
    }
  }

  object Root {
    def apply() = new {
      def onTransposed(_transposedStyle: DoxTableConfigTransposed.type => DoxTableConfigTransposed) = {
        nodeRoot().transposedStyle(_transposedStyle)
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

  object Index {
    def apply() = new {
      def width(_width: Option[Double]) = {
        nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.NONE).width(_width)
      }
    }
    def apply(name: String) = new {
      def width(_width: Option[Double]) = {
        nodeWritable(DoxTableKeyNodeType.INDEX).config(DoxTableKeyConfig.NONE.name(name)).width(_width)
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

  protected def nodeRoot() = {
    new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, configExt(DoxTableKeyConfig.NONE.name("Root")), Seq.empty) with Writeable {
      def transposedStyle(_styleOption: DoxTableConfigTransposed.type => DoxTableConfigTransposed) = {
        val transposedConfig = _styleOption(DoxTableConfigTransposed)
        new DoxTableKeyNode(
          nodeType,
          config
            .setCategoryWidth(transposedConfig.columnWidthCategory)
            .setDataWidthTransposed(transposedConfig.columnWidthData)
            .setDataAlignmentTransposed(transposedConfig.alignmentData),
          children) with Writeable
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
    DoxTableKeyConfigExtended(base, None, None)
  }
}