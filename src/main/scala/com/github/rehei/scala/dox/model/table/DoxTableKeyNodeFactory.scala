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
      new DoxTableKeyNode(this.textHeadOption, this.textBodyStrategyOption, this.format, this.children ++ additionalChildren) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.textHeadOption, this.textBodyStrategyOption, this.format, this.children ++ additionalChildren) with Writeable
    }
  }

  object Table extends {
    def create(node: DoxTableKeyNode) = {
      DoxTable[T](node)
    }
  }

  object Root {
    def apply() = {
      new DoxTableKeyNode(None, None, DoxTableKeyNodeFormat.CENTER, Seq.empty) with Writeable
    }
  }

  object Index {
    def apply(_config: DoxTableKeyConfigFixed.type => DoxTableKeyConfigFixed) = {
      val config = _config(DoxTableKeyConfigFixed)
      DoxTableKeyNode(Some(TextFactory.text("#")), Some(new DoxTableKeyNodeValueStrategy.ByRowIndex(config.width)), config.alignment, Seq.empty)
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
      DoxTableKeyNode(Some(TextFactory.text("#")), Some(new DoxTableKeyNodeValueStrategy.ByRowIndex(width)), DoxTableKeyNodeFormat.CENTER, Seq.empty)
    }
  }

  object Space {
    def apply() = {
      create(0)
    }
    def apply(width: Double) = {
      create(width)
    }
    protected def create(width: Double) = {
      DoxTableKeyNode(None, Some(new DoxTableKeyNodeValueStrategy.Spacing(width)), DoxTableKeyNodeFormat.CENTER, Seq.empty)
    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfigTransient.type => DoxTableKeyConfigTransient) = {
      val config = _config(DoxTableKeyConfigTransient)
      new DoxTableKeyNode(config.nameAST, None, config.alignment, Seq.empty) with Writeable
    }
  }

  object Value {
    def apply(_config: DoxTableKeyConfigFixed.type => DoxTableKeyConfigFixed) = new {
      val config = _config(DoxTableKeyConfigFixed)

      def finalize(callback: Query[T] => Query[_]) = {
        val query = callback(new Query[T])
        DoxTableKeyNode(config.nameAST, Some(new DoxTableKeyNodeValueStrategy.ByQuery(config.width, query)), config.alignment, Seq.empty)
      }
      def finalizeIndex(index: Int) = {
        DoxTableKeyNode(config.nameAST, Some(new DoxTableKeyNodeValueStrategy.BySequenceIndex(config.width, index)), config.alignment, Seq.empty)
      }
      def finalizeQueryMap(callback: Query[T] => Query[_], key: String) = {
        val query = callback(new Query[T])
        DoxTableKeyNode(config.nameAST, Some(new DoxTableKeyNodeValueStrategy.ByQueryAndMapKey(config.width, query, key)), config.alignment, Seq.empty)
      }
    }
  }

}