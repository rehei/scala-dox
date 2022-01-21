package com.github.rehei.scala.dox.model.table

case class DoxTableSequence(sequence: Seq[DoxTable[_]]) {

  def totalWidth(defaultWidth: Double) = {
    sequence.map(_.root.leavesRecursive().map(_.config.widthOption.getOrElse(defaultWidth))).maxBy(_.sum).sum
  }
  def tabcolSeps() = {
    sequence.map(_.root.leavesRecursive().size).max * 2
  }
}