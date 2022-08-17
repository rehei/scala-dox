package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

  val SUPPORT = 100
  
  trait Writeable extends DoxTableKeyNode {
    def append(additionalChildren: DoxTableKeyNode*) = {
      new DoxTableKeyNode(this.strategyOption, this.config, this.children ++ additionalChildren) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.strategyOption, this.config, this.children ++ additionalChildren) with Writeable
    }
  }

  object Table extends {
    def create(node: DoxTableKeyNode) = {
      DoxTable[T](node)
    }
  }

  object Root {
    def apply() = {
      nodeWritable(None).config(_.name("Root").alignment(_.CENTER))
    }
  }

  object Index {
    def apply(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      node(Some(DoxTableKeyNodeValueStrategy.ByRowIndex(10))).config(_config)
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
      DoxTableKeyNode(Some(DoxTableKeyNodeValueStrategy.ByRowIndex(width)), null, Seq.empty)
    }
  }

  object Blank {
    def apply() = {
      node(None).config(m => DoxTableKeyConfig.NO_NAME.alignment(_.CENTER).width(0.001))
    }
    def apply(widthOption: Option[Double]) = {
      val config = DoxTableKeyConfig(None, DoxTableAlignment.CENTER, widthOption)
      node(None).config(config)
    }
    def apply(_config: DoxTableKeyConfig.NO_NAME.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(None, _config(DoxTableKeyConfig.NO_NAME), Seq.empty) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(Some(DoxTableKeyNodeValueStrategy.ByQuery(SUPPORT, query)), _config(DoxTableKeyConfig.NO_NAME), Seq.empty)
        }
      }
    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(None, _config(DoxTableKeyConfig), Seq.empty) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(Some(DoxTableKeyNodeValueStrategy.ByQuery(SUPPORT, query)), _config(DoxTableKeyConfig), Seq.empty)
        }
        def finalizeIndex(index: Int) = {
          DoxTableKeyNode(Some(DoxTableKeyNodeValueStrategy.BySequenceIndex(SUPPORT, index)), _config(DoxTableKeyConfig), Seq.empty)
        }
      }
    }
  }

  protected def nodeWritable(_strategyOption: Option[DoxTableKeyNodeValueStrategy]) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(_strategyOption, _config(DoxTableKeyConfig), Seq.empty) with Writeable
    }
  }

  protected def node(_strategyOption: Option[DoxTableKeyNodeValueStrategy]) = new {
    def config(_config: DoxTableKeyConfig.type => DoxTableKeyConfig) = {
      DoxTableKeyNode(_strategyOption, _config(DoxTableKeyConfig), Seq.empty)
    }
    def config(_config: DoxTableKeyConfig) = {
      DoxTableKeyNode(_strategyOption, _config, Seq.empty)
    }
  }

}