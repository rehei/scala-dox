package com.github.rehei.scala.dox.model.tree

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.macros.Query

object MyDoxNodeFactory {

  trait Writeable extends MyDoxNode {

    def append(additionalChildren: MyDoxNode*) = {
      this.copy(children = children ++ additionalChildren)
    }

  }

  object Root {
    def apply(name: String) = {
      new MyDoxNode(MyDoxNodeType.ROOT, DoxTableKeyConfig.NONE.name(name), Seq.empty) with Writeable
    }
  }

  object Whitespace {
    def apply() = {
      new MyDoxNode(MyDoxNodeType.EMPTY, DoxTableKeyConfig.NONE, Seq.empty)
    }
  }

  object Index {
    def apply() = {
      new MyDoxNode(MyDoxNodeType.INDEX, DoxTableKeyConfig.NONE, Seq.empty) with Writeable
    }
  }

  object Node {
    def apply(config: DoxTableKeyConfig) = {
      new MyDoxNode(MyDoxNodeType.INTERMEDIATE, config, Seq.empty) with Writeable {
        def finalize(query: Query[_]) = {
          MyDoxNode(MyDoxNodeType.key(query), config, Seq.empty)
        }
      }
    }
  }

}