package com.github.rehei.scala.dox.control.tex

import java.text.DecimalFormat

object ColumnType {
  private val baseString = """\arraybackslash}p"""
  def l(size: Double) = """>{\raggedright""" + baseString + sizeString(size)
  def c(size: Double) = """>{\centering""" + baseString + sizeString(size)
  def r(size: Double) = """>{\raggedleft""" + baseString + sizeString(size)
  def lMinipage(text:String) = s"""\\begin{flushleft}${text}\\end{flushleft}"""
  def cMinipage(text:String) = s"""\\begin{center}${text}\\end{center}"""
  def rMinipage(text:String) = s"""\\begin{flushright}${text}\\end{flushright}"""
  def numeric(size: Double) = "S[table-number-alignment=center, table-column-width=" + size + "cm]"
  protected def sizeString(size: Double) = {
    val df = new DecimalFormat("#")
    df.setMinimumIntegerDigits(1)
    df.setMaximumFractionDigits(3)
    val stringValue = df.format(size)

    "{" + stringValue + "cm}"
  }
}