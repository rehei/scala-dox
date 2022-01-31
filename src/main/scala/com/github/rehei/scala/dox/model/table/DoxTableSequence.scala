package com.github.rehei.scala.dox.model.table

case class DoxTableSequence(sequence: Seq[DoxTableMatrix[_]]) {

  def totalWidth(defaultWidth: Double) = {
    sequence.map(_.totalWidth(defaultWidth)).max
  }

  def totalSeparatorCount() = {
    sequence.map(_.totalSeparatorCount()).max
  }

}