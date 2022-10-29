package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxViewModelTable
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.DoxViewModelEquation

object DoxBuilderEquation {
  def label(_labelOption: Option[DoxReferencePersistentEquation]) = new {
    def data(_equation: String) = {
      DoxViewModelEquation(DoxEquation(_equation), _labelOption)
    }
    def data(_equation: TextAST) = {
      DoxViewModelEquation(DoxEquation(Text2TEX(true).generate(_equation)), _labelOption)
    }
  }
}