package com.github.rehei.scala.dox.model.table

object DoxTableKeyConfigExtended {
  val NONE = DoxTableKeyConfigExtended(DoxTableKeyConfig.name("").alignment(_.CENTER), None)
}

case class DoxTableKeyConfigExtended(base: DoxTableKeyConfig, widthOption: Option[Double]) {

  def setCategoryWidth(_width: Option[Double]) = {
    this.copy(widthOption = _width)
  }

}