package com.github.rehei.scala.dox.control.tex

abstract class TexCommandInline(val args: TexSeq) extends AbstractTexCommand[TexCommandInline](args) {

  protected def create(in: TexSeq) = {
    new TexCommandInline(args.append(in)) {
      override def name() = TexCommandInline.this.name()
    }
  }

}