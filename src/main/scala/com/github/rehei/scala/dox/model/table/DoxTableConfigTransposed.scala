package com.github.rehei.scala.dox.model.table

object DoxTableConfigTransposed {

  val NONE = new DoxTableConfigTransposed(None, None, DoxTableAlignment.NONE)

  def categoryWidth(_widthCategory: Option[Double]) = new {
    def dataWidth(_widthData: Option[Double]) = new {
      def dataAlignment(_alignmentFunc: DoxTableAlignment.type => DoxTableAlignment) = {
        DoxTableConfigTransposed(_widthCategory, _widthData, _alignmentFunc(DoxTableAlignment))
      }
    }
  }

}

case class DoxTableConfigTransposed(columnWidthCategory: Option[Double], columnWidthData: Option[Double], alignmentData: DoxTableAlignment) {

}