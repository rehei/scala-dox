package com.github.rehei.scala.dox.control.tex

import scala.collection.Seq

object TexEscape {

  protected case class Replacement(matching: String, replacement: String)

  protected val replaceStrong = {
    Seq(
      Replacement("\\", "\\textbackslash "),
      Replacement("~", "\\~{}"),
      Replacement("^", "\\^{}"))
  }

  protected val replaceWeak = {
    Seq("_", "$", "%", "#", "&", "{", "}")
  }

  def escape(value: String) = {
    var tmp = value

    for (replacement <- replaceStrong) {
      tmp = tmp.replace(replacement.matching, replacement.replacement)
    }

    for (replacement <- replaceWeak) {
      tmp = tmp.replace(replacement, "\\" + replacement)
    }

    tmp
  }

}