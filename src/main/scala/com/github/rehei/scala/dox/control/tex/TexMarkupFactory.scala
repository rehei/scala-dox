package com.github.rehei.scala.dox.control.tex

class TexMarkupFactory(protected val ast: TexAST) {

  def &(in: Int) = TexValue(in.toString())
  def &(in: String) = TexValue(in)
  def &(in: TexOption) = in
  def &(in: TexCommandInline) = in

  def ###(in: String) = TexOption(in)

  protected lazy val __$ = new TexBuilderEnvironment(this)
  protected lazy val __\\ = new TexBuilderCommandInline(ast)

  def \ = new TexBuilderCommand(ast)
  def \\ = __\\
  def $ = __$

  def escape(value: String) = {
    TexEscape.escape(value)
  }

}