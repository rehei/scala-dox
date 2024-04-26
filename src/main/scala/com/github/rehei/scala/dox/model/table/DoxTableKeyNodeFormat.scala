package com.github.rehei.scala.dox.model.table

object DoxTableKeyNodeFormat {

  trait Writeable extends DoxTableKeyNodeFormat {

    val ROTATE_90 = {
      DoxTableKeyNodeFormat(this.alignment, Some(90))
    }

    val ROTATE_45 = {
      DoxTableKeyNodeFormat(this.alignment, Some(45))
    }

  }

  val LEFT = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignLeft, None) with Writeable
  val RIGHT = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignRight, None) with Writeable
  val CENTER = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignCenter, None) with Writeable
  val NUMERIC = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignNumeric, None) with Writeable
  
}

case class DoxTableKeyNodeFormat protected (alignment: DoxTableKeyNodeAlignment, rotateOption: Option[Int])