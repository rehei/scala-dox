package com.github.rehei.scala.dox.model.table

object DoxTableKeyConfigExtended extends DoxTableKeyConfigExtended(DoxTableKeyConfig.NONE, None) {
  val NONE = this
}

case class DoxTableKeyConfigExtended(base: DoxTableKeyConfig, width: Option[Double]) {

  def setCategoryWidth(_width: Option[Double]) = {
    this.copy(width = _width)
  }

}