package com.github.rehei.scala.dox.model.table

class DoxTableStringConversionDefault extends DoxTableStringConversion {

  def render(model: Any): String = {
    if (model == null) {
      ""
    } else {
      model.toString()
    }
  }

}