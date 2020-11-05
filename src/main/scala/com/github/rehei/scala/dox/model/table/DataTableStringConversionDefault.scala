package com.github.rehei.scala.dox.model.table

class DataTableStringConversionDefault extends DataTableStringConversion {

  def render(model: Any): String = {
    if (model == null) {
      ""
    } else {
      model.toString()
    }
  }

}