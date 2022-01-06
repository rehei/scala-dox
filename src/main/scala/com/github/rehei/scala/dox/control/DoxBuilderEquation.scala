package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxEquation

object DoxBuilderEquation {
  def label(_labelOption: Option[DoxReferencePersistentEquation]) = new {
    def data(_equation: String) = {
      DoxEquation(_equation, _labelOption)
    }
  }
}