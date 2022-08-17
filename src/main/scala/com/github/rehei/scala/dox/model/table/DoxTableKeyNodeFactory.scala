package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query
import scala.reflect.ClassTag
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableKeyNodeFactory[T <: AnyRef](implicit classTag: ClassTag[T]) {

  object DoxTableKeyConfigFixed {
    def name(in: String) = {
      nameInternal(TextFactory.text(in))
    }
    def name(in: TextAST) = {
      nameInternal(in)
    }
    protected def nameInternal(in: TextAST) = new {
      def alignment(_alignment: DoxTableAlignment.type => DoxTableAlignment) = new {
        def widthDefault() = {
          width(2)
        }
        def width(width: Double) = {
          DoxTableKeyConfigFixed(in, _alignment(DoxTableAlignment), width)
        }
      }
    }
  }

  object DoxTableKeyConfigTransient {
    def name(in: String) = {
      nameInternal(TextFactory.text(in))
    }
    def name(in: TextAST) = {
      nameInternal(in)
    }
    protected def nameInternal(in: TextAST) = new {
      def alignment(_alignment: DoxTableAlignment.type => DoxTableAlignment) = {
        DoxTableKeyConfigTransient(in, _alignment(DoxTableAlignment))
      }
    }
  }

  case class DoxTableKeyConfigFixed(name: TextAST, alignment: DoxTableAlignment, width: Double) {
    val nameAST = Some(name)
    val alignmentOption = Some(alignment)
  }
  case class DoxTableKeyConfigTransient(name: TextAST, alignment: DoxTableAlignment) {
    val nameAST = Some(name)
    val alignmentOption = Some(alignment)
  }

  val SUPPORT = 100

  trait Writeable extends DoxTableKeyNode {
    def append(additionalChildren: DoxTableKeyNode*) = {
      new DoxTableKeyNode(this.textHeadOption, this.textBodyStrategyOption, this.alignment, this.children ++ additionalChildren) with Writeable
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      new DoxTableKeyNode(this.textHeadOption, this.textBodyStrategyOption, this.alignment, this.children ++ additionalChildren) with Writeable
    }
  }

  object Table extends {
    def create(node: DoxTableKeyNode) = {
      DoxTable[T](node)
    }
  }

  object Root {
    def apply() = {
      new DoxTableKeyNode(None, None, None, Seq.empty) with Writeable
    }
  }

  object Index {
    def apply(_config: DoxTableKeyConfigFixed.type => DoxTableKeyConfigFixed) = {
      val config = _config(DoxTableKeyConfigFixed)
      DoxTableKeyNode(Some(TextFactory.text("#")), Some(DoxTableKeyNodeValueStrategy.ByRowIndex(config.width)), config.alignmentOption, Seq.empty)
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
      DoxTableKeyNode(Some(TextFactory.text("#")), Some(DoxTableKeyNodeValueStrategy.ByRowIndex(width)), Some(DoxTableAlignment.CENTER), Seq.empty)
    }
  }

  object Space {
    def apply() = {
      DoxTableKeyNode(None, Some(DoxTableKeyNodeValueStrategy.Spacing()), None, Seq.empty)
    }
  }

  object Blank {
    def apply(_config: DoxTableKeyConfig.NO_NAME.type => DoxTableKeyConfig) = {
      new DoxTableKeyNode(None, None, None, Seq.empty) with Writeable {
        def finalize(callback: Query[T] => Query[_]) = {
          val query = callback(new Query[T])
          DoxTableKeyNode(None, Some(DoxTableKeyNodeValueStrategy.ByQuery(SUPPORT, query)), None, Seq.empty)
        }
      }
    }
  }

  object Node {
    def apply(_config: DoxTableKeyConfigTransient.type => DoxTableKeyConfigTransient) = {
      val config = _config(DoxTableKeyConfigTransient)
      new DoxTableKeyNode(config.nameAST, None, config.alignmentOption, Seq.empty) with Writeable
    }
  }

  object Fixed {
    def apply(_config: DoxTableKeyConfigFixed.type => DoxTableKeyConfigFixed) = new {

      val config = _config(DoxTableKeyConfigFixed)

      def finalize(callback: Query[T] => Query[_]) = {
        val query = callback(new Query[T])
        DoxTableKeyNode(config.nameAST, Some(DoxTableKeyNodeValueStrategy.ByQuery(config.width, query)), config.alignmentOption, Seq.empty)
      }
      def finalizeIndex(index: Int) = {
        DoxTableKeyNode(config.nameAST, Some(DoxTableKeyNodeValueStrategy.BySequenceIndex(config.width, index)), config.alignmentOption, Seq.empty)
      }

    }

  }

}