package com.github.rehei.scala.dox.i18n

object DoxI18N {

  val DE_SHORT = {
    equation("Gl.").figure("Abb.").table("Tab.")
  }

  val EN_SHORT = {
    equation("Eq.").figure("Fig.").table("Table")
  }

  def equation(inputEquation: String) = new {
    def figure(inputFigure: String) = new {
      def table(inputTable: String) = {
        new DoxI18N(inputEquation, inputFigure, inputTable)
      }
    }
  }

}

class DoxI18N(val equation: String, val figure: String, val table: String)