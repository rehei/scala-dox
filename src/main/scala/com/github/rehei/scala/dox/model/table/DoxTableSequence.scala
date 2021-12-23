package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableSequence(sequence: Seq[DoxTable[_]]) {

  def totalWidth(defaultWidth: Double, transposedCategoryWidth: Double, transposedDefaultWidth: Double, transposed: Boolean) = {
    if (transposed) {
      transposedWidth(transposedCategoryWidth, transposedDefaultWidth)
    } else {
      sequence.map(_.root.leavesWidths(defaultWidth)).maxBy(_.sum).sum
    }
  }
  
  def tabcolSeps(transposed: Boolean) = {
    if (transposed) {
      (transposedColumnAmount() + 1) * 2
    } else {
      sequence.map(_.root.leavesAmount()).max * 2
    }
  }

  protected def transposedWidth(columnSizeCategory: Double, columnSizeDefault: Double) = {
    sequence.map(
      model => {
        model.root.config.width.getOrElse(columnSizeCategory) +
          model.root.config.transposedWidth.getOrElse(columnSizeDefault) +
          transposedColumnAmount()
      }).max

  }
  protected def transposedColumnAmount() = {
    sequence.map(_.transposed.list().map(_.data.length).max).max
  }
}