package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST

case class DoxTableMulti(models: Seq[DoxTable[_]]) {
  protected val DEFAULT_WIDTH = 2.0

  val columnsMaxAmount = models.map(_.root.leavesAmount()).max
  val columnsMaxWidths = models.map(_.root.leavesWidths(DEFAULT_WIDTH)).maxBy(_.sum)

}