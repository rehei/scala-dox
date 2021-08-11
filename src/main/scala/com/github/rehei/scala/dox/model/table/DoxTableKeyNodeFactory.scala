package com.github.rehei.scala.dox.model.table

import scala.collection.Seq
import com.github.rehei.scala.macros.Query

object DoxTableKeyNodeFactory {

  trait Writeable extends DoxTableKeyNode {

    def append(additionalChildren: DoxTableKeyNode*) = {
      this.copy(children = children ++ additionalChildren)
    }

    def appendAll(additionalChildren: Seq[DoxTableKeyNode]) = {
      this.copy(children = children ++ additionalChildren)
    }

  }

  object Root {
    def apply(name: String) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.ROOT, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable
    }
  }

  object Whitespace {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.WHITESPACE, DoxTableKeyConfig.NONE, Seq.empty)
    }
  }

  object Index {
    def apply() = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INDEX, DoxTableKeyConfig.NONE, Seq.empty) with Writeable
    }
  }

  object Node {
    def apply(config: DoxTableKeyConfig) = {
      new DoxTableKeyNode(DoxTableKeyNodeType.INTERMEDIATE, config, Seq.empty) with Writeable {
        def finalize(query: Query[_]) = {
          DoxTableKeyNode(DoxTableKeyNodeType.key(query), config, Seq.empty)
        }
      }
    }
  }

}