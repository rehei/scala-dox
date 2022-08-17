package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.text.TextAST

object DoxTableKeyConfigTransient {
  def name(in: String) = {
    nameInternal(TextFactory.text(in))
  }
  def name(in: TextAST) = {
    nameInternal(in)
  }
  protected def nameInternal(in: TextAST) = new {
    def alignment(_alignment: DoxTableAlignment.type => DoxTableAlignment) = {
      DoxTableKeyConfigTransient(in, _alignment(DoxTableAlignment))
    }
  }
}

case class DoxTableKeyConfigTransient(name: TextAST, alignment: DoxTableAlignment) {
  val nameAST = Some(name)
  val alignmentOption = Some(alignment)
}