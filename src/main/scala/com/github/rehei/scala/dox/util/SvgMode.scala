package com.github.rehei.scala.dox.util

object SvgMode {

  object PNG extends SvgMode {

    def commandInkscape(variable: String) = {
      "--export-png=../" + directory + "/${" + variable + "%.*}.png"
    }

    def commandRSVG(variable: String) = {
      "-f png -o ../" + directory + "/${f}.png ${f}"
    }

    def directory = "tex-svg-png"

    def file(name: String) = {
      name + ".svg.png"
    }

  }

  object PDF extends SvgMode {

    def commandInkscape(variable: String) = {
      "--export-pdf=../" + directory + "/${" + variable + "%.*}.pdf"
    }

    def commandRSVG(variable: String) = {
      "-f pdf -o ../" + directory + "/${f}.pdf ${f}"
    }

    def directory = "tex-svg-pdf"

    def file(name: String) = {
      name + ".svg.pdf"
    }

  }

  object EPS extends SvgMode {

    def commandInkscape(variable: String) = {
      "--export-eps=../" + directory + "/${" + variable + "%.*}.eps"
    }

    def commandRSVG(variable: String) = {
      "-f eps -o ../" + directory + "/${f}.pdf ${f}"
    }

    def directory = "tex-svg-eps"

    def file(name: String) = {
      name + ".svg.eps"
    }

  }

}

trait SvgMode {

  def commandInkscape(variable: String): String

  def commandRSVG(variable: String): String

  def directory: String

  def file(name: String): String

}