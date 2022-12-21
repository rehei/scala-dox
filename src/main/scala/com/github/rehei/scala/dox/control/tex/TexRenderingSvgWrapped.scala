package com.github.rehei.scala.dox.control.tex

trait TexRenderingSvgWrapped {

  protected val tmpAST = TexAST()
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)

  def generate() = {
    generateInternal()
    
    tmpAST
  }
  
  protected def generateInternal(): Unit

}