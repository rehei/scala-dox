package com.github.rehei.scala.dox.control.tex

case class TexCommandInline(ast: TexAST, name: String, args: TexSeq) extends AbstractTexCommand[TexCommandInline](name, args) {

  protected def create(in: TexSeq) = {
    this.copy(args = TexSeq(args.sequence ++ in.sequence))
  }

}