package com.github.rehei.scala.dox.model.table.tree

import com.github.rehei.scala.dox.model.tree.DoxNode
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.tree.DoxNodeFactory
import com.github.rehei.scala.dox.model.tree.DoxNodeType

class DoxTableTreeHeadRepository(root: DoxNode) {

  implicit class AbstractDoxNodeExt(base: DoxNode) {

    import DoxNodeFactory._
    
    def hasNonWhitespaceChildren() = {
      base.children.filterNot(_.nodeType == DoxNodeType.WHITESPACE).size > 0
    }

    def byLevel(level: Int): Seq[DoxNode] = {

      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }

    }

    def withWhitespace() = {
      withWhitespaceMax(base.depth())
    }

    protected def withWhitespaceMax(max: Int): DoxNode = {
      if (max > 0) {
        val extension = {
          if (base.children.isEmpty) {
            Seq(Whitespace())
          } else {
            Seq.empty
          }
        }
        base.copy(children = (base.children ++ extension).map(_.withWhitespaceMax(max - 1)))
      } else {
        base.copy()
      }
    }

  }

  def list() = {

    val transformedRoot = root.withWhitespace()

    for (level <- Range.inclusive(1, transformedRoot.depth())) yield {
      DoxTableTreeHeadRow(transformedRoot.byLevel(level).map(m => DoxTableTreeHeadRowKey(m.config, m.width(), m.hasNonWhitespaceChildren())))
    }
  }

}