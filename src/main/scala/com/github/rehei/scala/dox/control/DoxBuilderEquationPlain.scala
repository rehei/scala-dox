package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxViewModelEquation
import com.github.rehei.scala.dox.model.DoxEquation
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.DoxViewModelEquationPlain

object DoxBuilderEquationPlain {

  def label(_labelOption: Option[DoxReferencePersistentEquation]) = new {
    def data(_equation: String) = {
      DoxViewModelEquationPlain(_equation, _labelOption)
    }
  }

}