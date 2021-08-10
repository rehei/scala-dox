package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.tree.MyDoxNode
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.tree.MyDoxNodeFactory
import com.github.rehei.scala.dox.model.tree.MyDoxNodeType

class DoxTableTreeHeadRepository(root: MyDoxNode) {

  implicit class AbstractDoxNodeExt(base: MyDoxNode) {

    import MyDoxNodeFactory._
    
    def hasNonWhitespaceChildren() = {
      base.children.filterNot(_.nodeType == MyDoxNodeType.WHITESPACE).size > 0
    }

    def byLevel(level: Int): Seq[MyDoxNode] = {

      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }

    }

    def withWhitespace() = {
      withWhitespaceMax(base.depth())
    }

    protected def withWhitespaceMax(max: Int): MyDoxNode = {
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
      TableHeadRow(transformedRoot.byLevel(level).map(m => TableHeadRowKey(m.config, m.width(), m.hasNonWhitespaceChildren())))
    }
  }

}