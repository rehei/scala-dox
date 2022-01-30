package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxEquation

class TexRenderingEquation(baseAST: TexAST, equation: DoxEquation) {
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  import tmpMarkup._

  def createEquationString() = {
    create()
    tmpAST.build()
  }

  protected def create() {
    $ { _ eqnarray } {
      \ plain { equation.equation }
      equation
        .label
        .map(reference => { \ label { reference.name } })
    }
  }
}