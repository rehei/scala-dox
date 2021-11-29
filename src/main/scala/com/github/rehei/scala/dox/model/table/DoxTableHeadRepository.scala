package com.github.rehei.scala.dox.model.table

class DoxTableHeadRepository(totalRoot: DoxTableKeyNode) {

  protected val WHITESPACE_DEFAULT_WIDTH = 0.1
  protected val factory = DoxTableKeyNodeFactory()
  protected val tableSupport = DoxTableSupport(totalRoot)
  tableSupport.checkValidity()
  val title = tableSupport.title
  val root = totalRoot.copy(children = tableSupport.noneTitleChildren)

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
            Seq(factory.Whitespace(base.config.width.getOrElse(WHITESPACE_DEFAULT_WIDTH)))
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