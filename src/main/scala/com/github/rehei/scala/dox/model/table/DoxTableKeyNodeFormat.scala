package com.github.rehei.scala.dox.model.table

object DoxTableKeyNodeFormat {

  trait Writeable extends DoxTableKeyNodeFormat {

    val ROTATE = {
      DoxTableKeyNodeFormat(this.alignment, true)
    }

  }

  val LEFT = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignLeft, false) with Writeable
  val RIGHT = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignRight, false) with Writeable
  val CENTER = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignCenter, false) with Writeable
  val NUMERIC = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.AlignNumeric, false) with Writeable
  
}

case class DoxTableKeyNodeFormat protected (alignment: DoxTableKeyNodeAlignment, isRotated: Boolean)