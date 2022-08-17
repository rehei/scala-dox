package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

object DoxTableKeyConfig {

  val NO_NAME = nameInternal(None)

  def name(in: String) = {
    nameInternal(Some(TextFactory.text(in)))
  }
  def name(in: TextAST) = {
    nameInternal(Some(in))
  }
  protected def nameInternal(in: Option[TextAST]) = new {
    def alignment(_alignment: DoxTableAlignment.type => DoxTableAlignment) = {
      new DoxTableKeyConfig(in, _alignment(DoxTableAlignment), None) {
        def width(in: Double) = {
          this.copy(widthOption = Some(in))
        }
      }
    }
  }
}

case class DoxTableKeyConfig(protected [table] val textOption: Option[TextAST], alignment: DoxTableAlignment, widthOption: Option[Double]) {
  
  val text = textOption.getOrElse(TextFactory.NONE)
  
}


