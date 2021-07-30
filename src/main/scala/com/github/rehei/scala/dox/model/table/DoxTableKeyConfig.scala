package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

case class DoxTableKeyConfig(
  text:           TextAST,
  alignment:      DoxTableAlignment,
  dynamic:        Boolean,
  rendering:      DoxTableStringConversion,
  categoryOption: Option[DoxTableKeyCategory]) {

  def name(in: String) = {
    this.copy(text = TextFactory.text(in))
  }
  def name(in: TextAST) = {
    this.copy(text = in)
  }
}

