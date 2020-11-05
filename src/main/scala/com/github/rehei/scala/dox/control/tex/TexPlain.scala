package com.github.rehei.scala.dox.control.tex

case class TexPlain(ast: TexAST, input: String) extends AbstractTexObject {
  ast.append(this)
  def generate() = {
    input
  }
}