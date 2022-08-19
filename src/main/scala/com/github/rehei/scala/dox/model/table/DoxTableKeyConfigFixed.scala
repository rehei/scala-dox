package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.text.TextAST

object DoxTableKeyConfigFixed {
  def unnamed() = {
    nameInternal(None)
  }
  def name(in: String) = {
    nameInternal(Some(TextFactory.text(in)))
  }
  def name(in: TextAST) = {
    nameInternal(Some(in))
  }
  protected def nameInternal(in: Option[TextAST]) = new {
    def alignment(_alignment: DoxTableKeyNodeFormat.type => DoxTableKeyNodeFormat) = new {
      def widthDefault() = {
        width(2)
      }
      def width(width: Double) = {
        DoxTableKeyConfigFixed(in, _alignment(DoxTableKeyNodeFormat), width)
      }
    }
  }
}

case class DoxTableKeyConfigFixed(nameAST: Option[TextAST], alignment: DoxTableKeyNodeFormat, width: Double) {
  val alignmentOption = Some(alignment)
}