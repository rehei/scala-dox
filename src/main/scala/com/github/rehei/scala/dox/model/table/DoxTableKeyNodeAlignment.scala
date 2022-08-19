package com.github.rehei.scala.dox.model.table

object DoxTableKeyNodeAlignment {

  val LEFT = DoxTableKeyNodeAlignment("LEFT")
  val RIGHT = DoxTableKeyNodeAlignment("RIGHT")
  val CENTER = DoxTableKeyNodeAlignment("CENTER")
  val NUMERIC = DoxTableKeyNodeAlignment("NUMERIC")

}

case class DoxTableKeyNodeAlignment(name: String)
  
