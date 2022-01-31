package com.github.rehei.scala.dox.model.table

case class TreeWithWhitespaceBalancing() {

  protected val factory = DoxTableKeyNodeFactory()

  def fill(node: DoxTableKeyNode) = {
    fillTreeWithWhitespaceNodesByLevel(node, node.depth())
  }

  protected def fillTreeWithWhitespaceNodesByLevel(node: DoxTableKeyNode, max: Int): DoxTableKeyNode = {
    if (max > 0) {
      val extension = {
        if (node.children.isEmpty) {
          Seq(factory.Blank(node.config.widthOption))
        } else {
          Seq.empty
        }
      }
      node.copy(children = (node.children ++ extension).map(fillTreeWithWhitespaceNodesByLevel(_, max - 1)))
    } else {
      node.copy()
    }
  }

}