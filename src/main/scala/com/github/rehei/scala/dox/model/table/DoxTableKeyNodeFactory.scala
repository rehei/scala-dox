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
      new DoxTableKeyNode(this.strategy, this.config, this.children ++ additionalChildren) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.strategy, this.config, this.children ++ additionalChildren) with Writeable
    }
  }

  object Table extends {
    def create(node: DoxTableKeyNode) = {
      DoxTable[T](node)
    }
  }

  object Root {
    def apply() = {
      nodeWritable(DoxTableKeyNodeValueStrategy.root()).config(_.name("Root").alignment(_.CENTER))
    }
  }

  object Index {
    def apply(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      node(DoxTableKeyNodeValueStrategy.byRowIndex()).config(_config)
    }
  }

  object IndexDefault {
    def small() = {
      create(0.5)
    }
    def big() = {
      create(0.7)
    }
    protected def create(width: Double) = {
      node(DoxTableKeyNodeValueStrategy.byRowIndex()).config(_.name("#").alignment(_.CENTER).width(width))
    }
  }

  object Blank {
    def apply() = {
      node(DoxTableKeyNodeValueStrategy.blank()).config(m => DoxTableKeyConfig.NO_NAME.alignment(_.CENTER).width(0.001))
    }
    def apply(widthOption: Option[Double]) = {
      val config = DoxTableKeyConfig(None, DoxTableAlignment.CENTER, widthOption)
      node(DoxTableKeyNodeValueStrategy.blank()).config(config)
    }
    def apply(_config: DoxTableKeyConfig.NO_NAME.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeValueStrategy.blank(), _config(DoxTableKeyConfig.NO_NAME), Seq.empty) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(DoxTableKeyNodeValueStrategy.byValueQuery(query), _config(DoxTableKeyConfig.NO_NAME), Seq.empty)
        }
      }
    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeValueStrategy.intermediate(), _config(DoxTableKeyConfig), Seq.empty) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(DoxTableKeyNodeValueStrategy.byValueQuery(query), _config(DoxTableKeyConfig), Seq.empty)
        }
        def finalizeIndex(index: Int) = {
          DoxTableKeyNode(DoxTableKeyNodeValueStrategy.bySequenceIndex(index), _config(DoxTableKeyConfig), Seq.empty)
        }
      }
    }
  }

  protected def nodeWritable(_nodeType: DoxTableKeyNodeValueStrategy) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(_nodeType, _config(DoxTableKeyConfig), Seq.empty) with Writeable
    }
  }

  protected def node(_nodeType: DoxTableKeyNodeValueStrategy) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      DoxTableKeyNode(_nodeType, _config(DoxTableKeyConfig), Seq.empty)
    }
    def config(_config: DoxTableKeyConfig) = {
      DoxTableKeyNode(_nodeType, _config, Seq.empty)
    }
  }

}