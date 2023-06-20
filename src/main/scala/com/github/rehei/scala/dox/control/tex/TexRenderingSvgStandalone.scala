package com.github.rehei.scala.dox.control.tex

case class TexRenderingSvgStandalone(include: String) {

  protected val tmpAST = TexAST()
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  import tmpMarkup._

  def generate() = {
    tex()

    tmpAST
  }

  protected def tex() {
    \ vspace { "4pt" };
    \ centering;
    \ includegraphics { include }
  }

}