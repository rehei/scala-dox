package com.github.rehei.scala.dox.model.table.tree

import scala.collection.Seq

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.macros.Query

object DoxTableNodeFactory {

  trait Writeable extends DoxTableNode {

    def append(additionalChildren: DoxTableNode*) = {
      this.copy(children = children ++ additionalChildren)
    }

    def appendAll(additionalChildren: Seq[DoxTableNode]) = {
      this.copy(children = children ++ additionalChildren)
    }

  }

  object Root {
    def apply(name: String) = {
      new DoxTableNode(DoxTableNodeType.ROOT, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable
    }
  }

  object Whitespace {
    def apply() = {
      new DoxTableNode(DoxTableNodeType.WHITESPACE, DoxTableKeyConfig.NONE, Seq.empty)
    }
  }

  object Index {
    def apply() = {
      new DoxTableNode(DoxTableNodeType.INDEX, DoxTableKeyConfig.NONE, Seq.empty) with Writeable
    }
  }

  object Node {
    def apply(config: DoxTableKeyConfig) = {
      new DoxTableNode(DoxTableNodeType.INTERMEDIATE, config, Seq.empty) with Writeable {
        def finalize(query: Query[_]) = {
          DoxTableNode(DoxTableNodeType.key(query), config, Seq.empty)
        }
      }
    }
  }

}