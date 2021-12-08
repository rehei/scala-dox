package com.github.rehei.scala.dox.model.table

object DoxTableKeyConfigExtended {
  val NONE = DoxTableKeyConfigExtended(DoxTableKeyConfig.NONE, None, None)
}

case class DoxTableKeyConfigExtended(base: DoxTableKeyConfig, width: Option[Double], transposedWidth: Option[Double]) {
  
  def setCategoryWidth(newWidth: Option[Double]) = {
    this.copy(width = newWidth)
  }
  def setDataWidthTransposed(newWidth: Option[Double]) = {
    this.copy(transposedWidth = newWidth)
  }
  def setDataAlignmentTransposed(_alignment: DoxTableAlignment) = {
    this.copy(base = base.copy(alignment = _alignment))
  }
  
}