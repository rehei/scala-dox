package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory

object DoxTableKeyConfig {
  def name(in: String) = {
    nameInternal(TextFactory.text(in))
  }
  def name(in: TextAST) = {
    nameInternal(in)
  }
  protected def nameInternal(in: TextAST) = new {
    def alignment(_alignment: DoxTableAlignment.type => DoxTableAlignment) = {
      new DoxTableKeyConfig(in, _alignment(DoxTableAlignment), None) {
        def width(in: Double) = {
          this.copy(widthOption = Some(in))
        }
      }
    }
  }
}

case class DoxTableKeyConfig(text: TextAST, alignment: DoxTableAlignment, widthOption: Option[Double]) {

  def width(in: Option[Double]) = {
    this.copy(widthOption = in)
  }

}


