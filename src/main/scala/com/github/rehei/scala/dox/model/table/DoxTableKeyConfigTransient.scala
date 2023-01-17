package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.text.TextAST

object DoxTableKeyConfigTransient {
  def unnamed() = {
    nameInternal(None).alignment(_.CENTER)
  }
  def name(in: String) = {
    nameInternal(Some(TextFactory.text(in)))
  }
  def name(in: TextAST) = {
    nameInternal(Some(in))
  }
  protected def nameInternal(in: Option[TextAST]) = new {
    def alignment(_alignment: DoxTableKeyNodeFormat.type => DoxTableKeyNodeFormat) = {
      DoxTableKeyConfigTransient(in, _alignment(DoxTableKeyNodeFormat))
    }
  }
}

case class DoxTableKeyConfigTransient(nameAST: Option[TextAST], alignment: DoxTableKeyNodeFormat) {
  val alignmentOption = Some(alignment)
}