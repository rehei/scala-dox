package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST

class DoxTableHeadRepository(root: DoxTableKeyNode) {

  protected val factory = DoxTableKeyNodeFactory()

  implicit class AbstractDoxNodeExt(base: DoxTableKeyNode) {

    def hasNonEmptyChildren() = {
      nonEmptyChildren(base.children).size > 0
    }

    def byLevel(level: Int): Seq[DoxTableKeyNode] = {
      if (level == 0) {
        Seq(base)
      } else {
        base.children.flatMap(_.byLevel(level - 1))
      }
    }

    def fillTreeWithWhitespaceNodes() = {
      fillTreeWithWhitespaceNodesByLevel(base.depth())
    }

    def withColumnSpace() = {
      base.addSpaces()
    }

    protected def fillTreeWithWhitespaceNodesByLevel(max: Int): DoxTableKeyNode = {
      if (max > 0) {
        val extension = {
          if (base.children.isEmpty) {
            Seq(factory.Whitespace(base.config.widthOption))
          } else {
            Seq.empty
          }
        }
        base.copy(children = (base.children ++ extension).map(_.fillTreeWithWhitespaceNodesByLevel(max - 1)))
      } else {
        base.copy()
      }
    }

  }

  def list() = {
    val transformedRoot = root.withColumnSpace().fillTreeWithWhitespaceNodes()
    for (
      level <- Range.inclusive(1, transformedRoot.depth());
      val filteredChildren = transformedRoot.byLevel(level)
    ) yield {
      DoxTableHeadRow(
        filteredChildren.map(m => DoxTableHeadRowKey(m, m.config, m.width(), m.hasNonEmptyChildren())))
    }
  }

  protected def nonEmptyChildren(children: Seq[DoxTableKeyNode]) = {
    children
      .filter(_.nodeType != DoxTableKeyNodeType.BLANK)
      .filter(_.nodeType != DoxTableKeyNodeType.WHITESPACE)
  }

}