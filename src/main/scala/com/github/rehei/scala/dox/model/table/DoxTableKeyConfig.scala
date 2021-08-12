package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

object DoxTableKeyConfig {
  val NONE = {
    DoxTableKeyConfig(TextFactory.NONE, DoxTableAlignment.NONE, false)
  }
}

case class DoxTableKeyConfig(text: TextAST, alignment: DoxTableAlignment, dynamic: Boolean) {

  def name(in: String) = {
    this.copy(text = TextFactory.text(in))
  }
  def name(in: TextAST) = {
    this.copy(text = in)
  }
}

