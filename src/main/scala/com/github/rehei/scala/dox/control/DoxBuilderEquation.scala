package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX

object DoxBuilderEquation {
  def label(_labelOption: Option[DoxReferencePersistentEquation]) = new {
    def data(_equation: String) = {
      DoxEquation(_equation, _labelOption)
    }
    def data(_equation: TextAST) = {
      DoxEquation(Text2TEX.apply(true).generate(_equation), _labelOption)
    }
  }
}