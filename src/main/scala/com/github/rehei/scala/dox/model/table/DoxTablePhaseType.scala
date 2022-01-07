package com.github.rehei.scala.dox.model.table

object DoxTablePhaseType {

  val NONE = DoxTablePhaseType(0)
  val INIT = DoxTablePhaseType(1)
  val TUNING = DoxTablePhaseType(2)
  val SUMMARY = DoxTablePhaseType(3)

}

case class DoxTablePhaseType protected (val progression: Int)