package com.github.rehei.scala.dox.model.table

object DoxTableStringConversion {
  val NONE = new DoxTableStringConversion {
    def render(model: Any) = ""
  }
}
trait DoxTableStringConversion {

  def render(model: Any): String

}