package com.github.rehei.scala.dox.util

object SvgMode {

  object PNG extends SvgMode {

    def command(variable: String) = {
      "--export-png=../" + directory + "/${" + variable + "%.*}.png"
    }

    def directory = "tex-svg-png"

    def file(name: String) = {
      name + ".png"
    }

  }

  object PDF extends SvgMode {

    def command(variable: String) = {
      "--export-pdf=../" + directory + "/${" + variable + "%.*}.pdf"
    }

    def directory = "tex-svg-pdf"

    def file(name: String) = {
      name + ".pdf"
    }

  }

  object EPS extends SvgMode {

    def command(variable: String) = {
      "--export-eps=../" + directory + "/${" + variable + "%.*}.eps"
    }

    def directory = "tex-svg-eps"

    def file(name: String) = {
      name + ".eps"
    }

  }

}

trait SvgMode {

  def command(variable: String): String

  def directory: String

  def file(name: String): String

}