package com.github.rehei.scala.dox.control.tex

abstract class TexCommand(ast: TexAST, args: TexSeq) extends AbstractTexCommand[TexCommand](args) {

  ast.append(this)

  def apply(in: TexCommandInline) = {
    this.create(TexSeq(Seq(in)))
  }

  protected def create(in: TexSeq) = {
    ast.reverse()
    
    new TexCommand(ast, args.append(in)) {
      override def name() = TexCommand.this.name()
    }
  }

}

