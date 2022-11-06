package com.github.rehei.scala.dox.control.tex

import java.text.DecimalFormat

object ColumnType {

  def lMinipage(text: String) = environment("flushleft", text)
  def cMinipage(text: String) = environment("center", text)
  def rMinipage(text: String) = environment("flushright", text)

  def l(size: Double) = slashed("""\raggedright""", size)
  def c(size: Double) = slashed("""\centering""", size)
  def r(size: Double) = slashed("""\raggedleft""", size)

  def numeric(size: Double) = "S[table-number-alignment=center, table-column-width=" + size + "cm]"

  protected def slashed(command: String, size: Double) = {
    ">{" + command + """\arraybackslash""" + "}" + sizeString(size)
  }

  protected def sizeString(size: Double) = {
    val df = new DecimalFormat("#")
    df.setMinimumIntegerDigits(1)
    df.setMaximumFractionDigits(3)
    val stringValue = df.format(size)

    "p{" + stringValue + "cm}"
  }

  protected def environment(name: String, content: String) = {
    s"""\\begin{${name}}${content}\\end{${name}}"""
  }
}
