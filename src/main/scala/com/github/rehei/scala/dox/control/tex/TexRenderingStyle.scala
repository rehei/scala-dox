package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableKeyNodeAlignment

object TexRenderingStyle {

  object NONE extends TexRenderingStyle {
    def get(width: String, text: String) = {
      text
    }
    def multicolumnAlignment(alignment: DoxTableKeyNodeAlignment) = {
      alignment.texAlignmentColumnhead()
    }
    def minipageAlignment(alignment: DoxTableKeyNodeAlignment, text: String) = {
      text
    }
  }

  object MINIPAGE extends TexRenderingStyle {
    def get(width: String, text: String) = {
      "\\columnBox{" + width + "}{" + text + "}"
    }
    def multicolumnAlignment(alignment: DoxTableKeyNodeAlignment) = {
      "c"
    }
    def minipageAlignment(alignment: DoxTableKeyNodeAlignment, text: String) = {
      alignment.texAlignmentMinipage(text)
    }
  }

  object MINIPAGE_FRAMED extends TexRenderingStyle {
    def get(width: String, text: String) = {
      "\\columnBoxFramed{" + width + "}{" + text + "}"
    }
    def multicolumnAlignment(alignment: DoxTableKeyNodeAlignment) = {
      "c"
    }
    def minipageAlignment(alignment: DoxTableKeyNodeAlignment, text: String) = {
      alignment.texAlignmentMinipage(text)
    }
  }

}

abstract class TexRenderingStyle {
  def get(width: String, text: String): String
  def multicolumnAlignment(alignment: DoxTableKeyNodeAlignment): String
  def minipageAlignment(alignment: DoxTableKeyNodeAlignment, text: String): String
}

