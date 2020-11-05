package com.github.rehei.scala.dox.model.table

object DataTableAlignment {

  val LEFT = DataTableAlignment("LEFT")
  val RIGHT = DataTableAlignment("RIGHT")
  val CENTER = DataTableAlignment("CENTER")

}

case class DataTableAlignment protected (name: String)