package com.github.rehei.scala.dox.model.table.tree

import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig

class DoxTableTreeHeadRepository(root: DoxTableNode) {
  import DoxTableNodeFactory._

  implicit class AbstractDoxNodeExt(base: DoxTableNode) {

    def hasNonWhitespaceChildren() = {
      base.children.filterNot(_.nodeType == DoxTableNodeType.WHITESPACE).size > 0
    }

    def byLevel(level: Int): Seq[DoxTableNode] = {

      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }

    }

    def withWhitespace() = {
      withWhitespaceMax(base.depth())
    }

    protected def withWhitespaceMax(max: Int): DoxTableNode = {
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