package com.github.rehei.scala.dox.model.table

object DoxTableKeyConfigExtended {
  val NONE = DoxTableKeyConfigExtended(DoxTableKeyConfig.NONE, None)
}
case class DoxTableKeyConfigExtended(base: DoxTableKeyConfig, width: Option[Double]) {
  def setWidth(newWidth: Option[Double]) = {
    this.copy(width = newWidth)
  }
}