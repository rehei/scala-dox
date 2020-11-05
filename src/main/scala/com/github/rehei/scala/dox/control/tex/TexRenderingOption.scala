package com.github.rehei.scala.dox.control.tex

class TexRenderingOption(baseAST: TexAST) {
  
  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  def showFrame(enable: Boolean) {
    if (enable) {
      \ usepackage { "showframe" }
    }
  }

}