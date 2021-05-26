package com.github.rehei.scala.dox.util

object SvgMode {

  object PNG extends SvgMode {

    def command(variable: String) = {
      "--export-png=${" + variable + "%.*}.png"
    }

    def file(name: String) = {
      name + ".png"
    }

  }

  object PDF extends SvgMode {

    def command(variable: String) = {
      "--export-pdf=${" + variable + "%.*}.pdf"
    }

    def file(name: String) = {
      name + ".pdf"
    }

  }
  
  object EPS extends SvgMode {

    def command(variable: String) = {
      "--export-eps=${" + variable + "%.*}.eps"
    }

    def file(name: String) = {
      name + ".eps"
    }

  }

}

trait SvgMode {

  def command(variable: String): String

  def file(name: String): String

}