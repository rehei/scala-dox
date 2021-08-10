package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.tree.MyDoxNode
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.model.tree.MyDoxNodeFactory

class DoxTableTreeHeadRepository(root: MyDoxNode) {

  implicit class AbstractDoxNodeExt(base: MyDoxNode) {

    import MyDoxNodeFactory._

    def withWhitespace(max: Int): MyDoxNode = {

      if (max > 0) {

        val extension = {
          if (base.children.isEmpty) {
            Seq(Whitespace())
          } else {
            Seq.empty
          }
        }

        base.copy(children = (base.children ++ extension).map(_.withWhitespace(max - 1)))

      } else {
        base.copy()
      }
    }

    def byLevel(level: Int): Seq[MyDoxNode] = {

      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }

    }

  }

  def list() = {
    
    val update = root.withWhitespace(root.depth())
    
    for (level <- Range.inclusive(1, root.depth())) yield {
      TableHeadRow(update.byLevel(level).map(m => TableHeadRowKey(m.config, m.width())))
    }
  }

}