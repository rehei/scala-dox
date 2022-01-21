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
        filterBlankChildren(base.children.flatMap(_.byLevel(level - 1)))
      }
    }

    def withWhitespace() = {
      withWhitespaceMax(base.depth())
    }
    protected def filterBlankChildren(children: Seq[DoxTableKeyNode]) = {
      if (children.exists(_.nodeType == DoxTableKeyNodeType.BLANK)) {
        children
          .filterNot(_.nodeType == DoxTableKeyNodeType.BLANK)
          .filterNot(_.nodeType == DoxTableKeyNodeType.WHITESPACE)
          .filterNot(_.nodeType == DoxTableKeyNodeType.COLUMNSPACE)
      } else {
        children
      }
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

    for (level <- Range.inclusive(1, transformedRoot.depth()) if (!transformedRoot.byLevel(level).isEmpty)) yield {
      DoxTableHeadRow(
        transformedRoot
          .byLevel(level)
          .map(m => DoxTableHeadRowKey(m.config, m.width(), m.hasNonWhitespaceChildren())))
    }
  }

}