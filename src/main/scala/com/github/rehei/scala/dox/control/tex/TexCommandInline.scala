package com.github.rehei.scala.dox.control.tex

abstract class TexCommandInline(val inline: Boolean, val args: TexSeq) extends AbstractTexCommand[TexCommandInline](inline, args) {

  protected def create(in: TexSeq) = {
    new TexCommandInline(inline, args.append(in)) {
      override def name() = TexCommandInline.this.name()
    }
  }

}