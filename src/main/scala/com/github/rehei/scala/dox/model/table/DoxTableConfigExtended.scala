package com.github.rehei.scala.dox.model.table

object DoxTableConfigExtended {

  val NONE = new DoxTableConfigExtended(DoxTableAlignment.NONE, false, None, None)

  def dataAlignment(_alignmentFunc: DoxTableAlignment.type => DoxTableAlignment) = new {
    def withColumnSpacing(_hasMidrule: Boolean) = new {
      def transposedCategoryWidth(_widthCategory: Option[Double]) = new {
        def transposedDataWidth(_widthData: Option[Double]) = {
          DoxTableConfigExtended(_alignmentFunc(DoxTableAlignment), _hasMidrule, _widthCategory, _widthData)
        }
      }
    }
  }

}

case class DoxTableConfigExtended(alignmentData: DoxTableAlignment, hasMidrule: Boolean, columnWidthCategory: Option[Double], columnWidthData: Option[Double]) {

}