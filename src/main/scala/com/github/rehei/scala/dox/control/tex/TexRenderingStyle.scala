package com.github.rehei.scala.dox.control.tex

object TexRenderingStyle {

  object NONE extends TexRenderingStyle {
    def get(width: String, text: String, alignment: String) = {
      text
    }
  }

  object MINIPAGE extends TexRenderingStyle {
    def get(width: String, text: String, alignment: String) = {
      "\\columnBox{" + width + "}{" + text + "}{" + alignment + "}"
    }
  }

  object MINIPAGE_FRAMED extends TexRenderingStyle {
    def get(width: String, text: String, alignment: String) = {
      "\\columnBoxFramed{" + width + "}{" + text + "}{" + alignment + "}"
    }
  }

}

abstract class TexRenderingStyle {
  def get(width: String, text: String, alignment: String): String
}

