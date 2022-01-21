package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST

class DoxTableHeadRepository(root: DoxTableKeyNode) {

  protected val factory = DoxTableKeyNodeFactory()

  implicit class AbstractDoxNodeExt(base: DoxTableKeyNode) {

    def hasNonWhitespaceChildren() = {
      base.children.filterNot(_.nodeType == DoxTableKeyNodeType.WHITESPACE).size > 0
    }

    def byLevel(level: Int): Seq[DoxTableKeyNode] = {
      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }
    }

    def withWhitespace() = {
      withWhitespaceMax(base.depth())
    }

    protected def withWhitespaceMax(max: Int): DoxTableKeyNode = {
      if (max > 0) {
        val extension = {
          if (base.children.isEmpty) {
            Seq(factory.Whitespace(base.config.widthOption))
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
      DoxTableHeadRow(
        transformedRoot
          .byLevel(level)
          .map(m => DoxTableHeadRowKey(m.config, m.width(), m.hasNonWhitespaceChildren())))
    }
  }

}