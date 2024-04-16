package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxEquation

class TexRenderingEquation(equation: DoxEquation) {
  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  import tmpMarkup._

  def createEquationString() = {
    create()
    tmpAST.build()
  }

  protected def create() {
    equation.tag.map {
      tagName =>
        {
          $ { _ equation } {
            \ tag { tagName }
            \ plain { equation.equation }
          }
        }
    } getOrElse {
      $ { _ eqnarray } {
        \ plain { equation.equation }
      }
    }

  }
}