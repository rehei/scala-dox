package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

  trait Writeable extends DoxTableKeyNode {

    def append(additionalChildren: DoxTableKeyNode*) = {
      new DoxTableKeyNode(this.nodeType, this.config, appendToTitleOrChildren(children, additionalChildren)) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.nodeType, this.config, appendToTitleOrChildren(children, additionalChildren)) with Writeable
    }

    protected def appendToTitleOrChildren(children: Seq[DoxTableKeyNode], newChildren: Seq[DoxTableKeyNode]) = {
      children
        .headOption
        .map(head => {
          head.nodeType match {
            case DoxTableKeyNodeType.TITLE => {
              val firstTitleChild = addChildrenToUltimateTitle(head, newChildren)
              Seq(firstTitleChild) ++ children.drop(1)
            }
            case other => children ++ newChildren
          }
        }).getOrElse(newChildren)
    }

    protected def addChildrenToUltimateTitle(child: DoxTableKeyNode, newChildren: Seq[DoxTableKeyNode]): DoxTableKeyNode = {
      child
        .children
        .headOption
        .map(head => {
          head.nodeType match {
            case DoxTableKeyNodeType.TITLE => child.copy(children = Seq(addChildrenToUltimateTitle(head, newChildren)) ++ child.children.drop(1))
            case other                     => child.copy(children = child.children ++ newChildren)
          }
        }).getOrElse(child.copy(children = newChildren))
    }
  }

  object Table {
    def apply(node: DoxTableKeyNode) = {
      DoxTable[T](node).withColumnSpace
    }
  }

  object Root {
    def apply(_name: String, _title: Option[String]) = new {
      def onTransposed(_transposedStyle: DoxTableConfigTransposed.type => DoxTableConfigTransposed) = {
        rootNode(_transposedStyle)
      }
      protected def rootNode(_transposedStyle: DoxTableConfigTransposed.type => DoxTableConfigTransposed) = {
        val root = nodeRoot(_name).transposedStyle(_transposedStyle)
        _title.map {
          text => root.append(nodeWritable(DoxTableKeyNodeType.TITLE).config(DoxTableKeyConfig.NONE.name(text)).width(None))
        } getOrElse {
          root
        }
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

  protected def nodeRoot(_name: String) = {
    new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, configExt(DoxTableKeyConfig.NONE.name(_name)), Seq.empty) with Writeable {
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