package com.github.rehei.scala.dox.control.tex

import java.text.DecimalFormat

object ColumnType {
  
  def l(size: Option[Double]) = slashed("""\raggedright""", size)
  def c(size: Option[Double]) = slashed("""\centering""", size)
  def r(size: Option[Double]) = slashed("""\raggedleft""", size)
  
  def lMinipage(text:String) = s"""\\begin{flushleft}${text}\\end{flushleft}"""
  def cMinipage(text:String) = s"""\\begin{center}${text}\\end{center}"""
  def rMinipage(text:String) = s"""\\begin{flushright}${text}\\end{flushright}"""
  
  def numeric(size: Double) = "S[table-number-alignment=center, table-column-width=" + size + "cm]"
  
  protected def slashed(command: String, size: Option[Double]) = {
    ">{" + command + """\arraybackslash""" + "}p" + size.map(sizeString(_)).getOrElse("")
  }
  
  protected def sizeString(size: Double) = {
    val df = new DecimalFormat("#")
    df.setMinimumIntegerDigits(1)
    df.setMaximumFractionDigits(3)
    val stringValue = df.format(size)

    "{" + stringValue + "cm}"
  }
}
