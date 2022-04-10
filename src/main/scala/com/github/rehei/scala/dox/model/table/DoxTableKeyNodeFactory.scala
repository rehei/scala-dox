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
    def apply(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      node(DoxTableKeyNodeType.INDEX).config(_config)
    }
  }

  object IndexDefault {
    def apply() = {
      node(DoxTableKeyNodeType.INDEX).config(_.name("#").alignment(_.CENTER).width(0.5))
    }
  }

  object IndexBig {
    def apply() = {
      node(DoxTableKeyNodeType.INDEX).config(_.name("#").alignment(_.CENTER).width(0.7))
    }
  }

  object Blank {
    def apply() = {
      node(DoxTableKeyNodeType.BLANK).config(_.name(TextFactory.NONE).alignment(_.CENTER).width(0.001))
    }
    def apply(widthOption: Option[Double]) = {
      val config = DoxTableKeyConfig(TextFactory.NONE, DoxTableAlignment.CENTER, widthOption)
      node(DoxTableKeyNodeType.BLANK).config(config)
    }
    def apply(_config: DoxTableKeyConfig.NO_NAME.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.BLANK, _config(DoxTableKeyConfig.NO_NAME), Seq.empty, None) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(DoxTableKeyNodeType.BLANK, _config(DoxTableKeyConfig.NO_NAME), Seq.empty, Some(query))
        }
      }
    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INTERMEDIATE, _config(DoxTableKeyConfig), Seq.empty, None) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(DoxTableKeyNodeType.VALUE, _config(DoxTableKeyConfig), Seq.empty, Some(query))
        }
      }
    }
  }

  protected def nodeWritable(_nodeType: DoxTableKeyNodeType) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(_nodeType, _config(DoxTableKeyConfig), Seq.empty, None) with Writeable
    }
  }

  protected def node(_nodeType: DoxTableKeyNodeType) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      DoxTableKeyNode(_nodeType, _config(DoxTableKeyConfig), Seq.empty, None)
    }
    def config(_config: DoxTableKeyConfig) = {
      DoxTableKeyNode(_nodeType, _config, Seq.empty, None)
    }
  }

}