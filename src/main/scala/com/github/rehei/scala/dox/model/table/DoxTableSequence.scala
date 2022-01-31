package com.github.rehei.scala.dox.model.table

case class DoxTableSequence(sequence: Seq[DoxTableMatrix[_]]) {

  def totalWidth(defaultWidth: Double) = {
    sequence.map(_.dimension().map(_.widthOption.getOrElse(defaultWidth))).map(_.sum).max
  }

  def tabcolSeps() = {
    sequence.map(_.dimension().size).max * 2
  }

}