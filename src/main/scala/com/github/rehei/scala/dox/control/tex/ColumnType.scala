package com.github.rehei.scala.dox.control.tex

import java.text.DecimalFormat

object ColumnType {
  
  def l(size: Double) = slashed("""\raggedright""", size)
  def c(size: Double) = slashed("""\centering""", size)
  def r(size: Double) = slashed("""\raggedleft""", size)
  
  def lMinipage(text:String) = s"""\\begin{flushleft}${text}\\end{flushleft}"""
  def cMinipage(text:String) = s"""\\begin{center}${text}\\end{center}"""
  def rMinipage(text:String) = s"""\\begin{flushright}${text}\\end{flushright}"""
  
  def numeric(size: Double) = "S[table-number-alignment=center, table-column-width=" + size + "cm]"
  
  protected def slashed(command: String, size: Double) = {
    ">{" + command + """\arraybackslash""" + "}p" + sizeString(size)
  }
  
  protected def sizeString(size: Double) = {
    val df = new DecimalFormat("#")
    df.setMinimumIntegerDigits(1)
    df.setMaximumFractionDigits(3)
    val stringValue = df.format(size)

    "{" + stringValue + "cm}"
  }
}
