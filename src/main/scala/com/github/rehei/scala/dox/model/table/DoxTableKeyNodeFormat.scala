package com.github.rehei.scala.dox.model.table

object DoxTableKeyNodeFormat {

  trait Writeable extends DoxTableKeyNodeFormat {

    val ROTATE = {
      DoxTableKeyNodeFormat(this.alignment, true)
    }

  }

  val LEFT = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.LEFT, false) with Writeable
  val RIGHT = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.RIGHT, false) with Writeable
  val CENTER = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.CENTER, false) with Writeable
  val NUMERIC = new DoxTableKeyNodeFormat(DoxTableKeyNodeAlignment.NUMERIC, false) with Writeable
  
}

case class DoxTableKeyNodeFormat protected (alignment: DoxTableKeyNodeAlignment, isRotated: Boolean)