package com.github.rehei.scala.dox.model.table

case class DoxTableSequence(sequence: Seq[DoxTableMatrix]) {

  def totalWidth() = {
    sequence.map(_.totalWidth()).max
  }

  def totalSeparatorCount() = {
    sequence.map(_.totalSeparatorCount()).max
  }

}