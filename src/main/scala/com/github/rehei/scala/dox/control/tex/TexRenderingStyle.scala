package com.github.rehei.scala.dox.control.tex

object TexRenderingStyle {
  
  object NONE extends TexRenderingStyle {
    def get(width: String, text: String) = {
      "\\columnBox{" + width + "}{" + text + "}"
    }
  }
  
  object FRAMED extends TexRenderingStyle {
    def get(width: String, text: String) = {
      "\\columnBoxFramed{" + width + "}{" + text + "}"
    }
  }

}

abstract class TexRenderingStyle {
  def get(width: String, text: String): String
}

