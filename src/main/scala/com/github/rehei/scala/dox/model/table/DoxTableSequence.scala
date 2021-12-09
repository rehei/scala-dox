package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSequence(sequence: Seq[DoxTable[_]]) {

  def columnsMaxAmount = sequence.map(_.root.leavesAmount()).max
  def columnsMaxWidths(defaultWidth: Double) = sequence.map(_.root.leavesWidths(defaultWidth)).maxBy(_.sum)

}