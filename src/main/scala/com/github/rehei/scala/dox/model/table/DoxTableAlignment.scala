package com.github.rehei.scala.dox.model.table

object DoxTableAlignment {

  val NONE = DoxTableAlignment("")
  val LEFT = DoxTableAlignment("LEFT")
  val RIGHT = DoxTableAlignment("RIGHT")
  val CENTER = DoxTableAlignment("CENTER")
  val NUMERIC = DoxTableAlignment("NUMERIC")

}

case class DoxTableAlignment protected (name: String)