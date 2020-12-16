package com.github.rehei.scala.dox.control.tex

abstract class TexCommand(inline: Boolean, ast: TexAST, args: TexSeq) extends AbstractTexCommand[TexCommand](inline, args) {

  ast.append(this)

  def apply(in: TexCommandInline) = {
    this.create(TexSeq(Seq(in)))
  }

  protected def create(in: TexSeq) = {
    ast.reverse()
    
    new TexCommand(inline, ast, args.append(in)) {
      override def name() = TexCommand.this.name()
    }
  }

}

