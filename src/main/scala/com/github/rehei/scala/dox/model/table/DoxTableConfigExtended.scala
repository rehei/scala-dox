package com.github.rehei.scala.dox.model.table

object DoxTableConfigExtended {

  val NONE = new DoxTableConfigExtended(DoxTableAlignment.NONE, false, None, None)

  def dataAlignment(_alignmentFunc: DoxTableAlignment.type => DoxTableAlignment) = new {
    def withRowSpacing(_hasRowSpacing: Boolean) = new {
      def transposedCategoryWidth(_widthCategory: Option[Double]) = new {
        def transposedDataWidth(_widthData: Option[Double]) = {
          DoxTableConfigExtended(_alignmentFunc(DoxTableAlignment), _hasRowSpacing, _widthCategory, _widthData)
        }
      }
    }
  }

}

case class DoxTableConfigExtended(alignmentData: DoxTableAlignment, hasRowSpacing: Boolean, columnWidthCategory: Option[Double], columnWidthData: Option[Double]) {

}