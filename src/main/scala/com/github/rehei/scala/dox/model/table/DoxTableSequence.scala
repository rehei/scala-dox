package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSequence(models: Seq[DoxTable[_]]) {

  def columnsMaxAmount = models.map(_.root.leavesAmount()).max
  def columnsMaxWidths(defaultWidth: Double) = models.map(_.root.leavesWidths(defaultWidth)).maxBy(_.sum)

}