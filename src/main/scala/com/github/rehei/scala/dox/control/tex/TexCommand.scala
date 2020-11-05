package com.github.rehei.scala.dox.control.tex

case class TexCommand(ast: TexAST, name: String, args: TexSeq) extends AbstractTexCommand[TexCommand](name, args) {

  ast.append(this)

  def apply(in: TexCommandInline) = {
    this.create(TexSeq(Seq(in)))
  }

  protected def create(in: TexSeq) = {
    ast.reverse()
    this.copy(args = TexSeq(args.sequence ++ in.sequence))
  }

}

