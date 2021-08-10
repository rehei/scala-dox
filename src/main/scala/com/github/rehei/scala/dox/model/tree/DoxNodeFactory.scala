package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.macros.Query

object DoxNodeFactory {

  trait Writeable extends DoxNode {

    def append(additionalChildren: DoxNode*) = {
      this.copy(children = children ++ additionalChildren)
    }

  }

  object Root {
    def apply(name: String) = {
      new DoxNode(DoxNodeType.ROOT, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable
    }
  }
  
  object Whitespace {
    def apply() = {
      new DoxNode(DoxNodeType.WHITESPACE, DoxTableKeyConfig.NONE, Seq.empty)
    }
  }

  object Index {
    def apply() = {
      new DoxNode(DoxNodeType.INDEX, DoxTableKeyConfig.NONE, Seq.empty) with Writeable
    }
  }

  object Node {
    def apply(config: DoxTableKeyConfig) = {
      new DoxNode(DoxNodeType.INTERMEDIATE, config, Seq.empty) with Writeable {
        def finalize(query: Query[_]) = {
          DoxNode(DoxNodeType.key(query), config, Seq.empty)
        }
      }
    }
  }

}