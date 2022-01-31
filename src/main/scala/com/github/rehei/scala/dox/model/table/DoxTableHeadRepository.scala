package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST

class DoxTableHeadRepository(root: DoxTableKeyNode) {

  implicit class AbstractDoxNodeExt(base: DoxTableKeyNode) {

    def byLevel(level: Int): Seq[DoxTableKeyNode] = {
      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }
    }

    def withWhitespaceBalancing() = {
      val transformer = TreeWithWhitespaceBalancing()
      transformer.fill(base)
    }

    def withColumnSpace() = {
      val transformer = TreeWithSpacingBetween()
      transformer.addSpaces(base)
    }

  }

  def list() = {
    val transformedRoot = root.withColumnSpace().withWhitespaceBalancing()

    for (level <- Range.inclusive(1, transformedRoot.depth())) yield {
      val filteredChildren = transformedRoot.byLevel(level)
      DoxTableHeadRow(filteredChildren.map(m => DoxTableHeadRowKey(m, m.width(), m.hasNonEmptyChildren())))
    }
  }

}