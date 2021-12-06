package com.github.rehei.scala.dox.model.table

object DoxTableKeyConfigExtended {
  val NONE = DoxTableKeyConfigExtended(DoxTableKeyConfig.NONE, None, None)
}

case class DoxTableKeyConfigExtended(base: DoxTableKeyConfig, width: Option[Double], transposedWidth: Option[Double]) {
  
  def setWidth(newWidth: Option[Double]) = {
    this.copy(width = newWidth)
  }
  def setWidthTransposed(newWidth: Option[Double]) = {
    this.copy(transposedWidth = newWidth)
  }
  def setAlignment(_alignment: DoxTableAlignment) = {
    this.copy(base = base.copy(alignment = _alignment))
  }
  
}