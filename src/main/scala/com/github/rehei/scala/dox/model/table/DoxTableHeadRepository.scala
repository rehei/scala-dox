package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST

class DoxTableHeadRepository(root: DoxTableKeyNode) {

  protected val dirtySpacingEnabled = false

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
      if (dirtySpacingEnabled) {
        val transformer = TreeWithSpacingBetween()
        transformer.addSpaces(base)
      } else {
        base
      }
    }

  }

  def list() = {
    val transformedRoot = root.withColumnSpace().withWhitespaceBalancing()

    for (level <- Range.inclusive(1, transformedRoot.treeDepth())) yield {
      val filteredChildren = transformedRoot.byLevel(level)
      DoxTableHeadRow(filteredChildren.map(m => DoxTableHeadRowKey(m, m.treeBreadth(), m.hasAnyHeadDefinedChildren())))
    }
  }

}