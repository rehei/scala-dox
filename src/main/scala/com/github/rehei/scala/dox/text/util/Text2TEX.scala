package com.github.rehei.scala.dox.text.util

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObjectSubscript
import com.github.rehei.scala.dox.text.TextObjectSpace
import com.github.rehei.scala.dox.text.TextObject
import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.control.tex.TexEscape

object Text2TEX {

  import TexEscape._

  def generate(element: TextAST) = {
    element.sequence.map(asText(_)).mkString
  }

  protected def asText(text: TextObject) = {
    text match {
      case m: TextObjectDefault   => textDefault(m)
      case m: TextObjectSpace     => textSpace(m)
      case m: TextObjectSubscript => textSubscript(m)
    }
  }

  protected def textDefault(text: TextObjectDefault) = {
    escape(text.in)
  }

  protected def textSpace(text: TextObjectSpace) = {
    ("\\hspace*" * text.space) + " " + escape(text.in)
  }

  protected def textSubscript(text: TextObjectSubscript) = {
    "_{" + escape(text.in) + "}"
  }

}