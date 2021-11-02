package com.github.rehei.scala.dox.control.tex

abstract class TexEnvironment(val sequence: TexSeq) {

  def name(): String

  def apply(in: TexSeq) = {
    copy(in.sequence)
  }

  def apply(in: TexOption) = {
    copy(Seq(in))
  }

  def apply(in:TexArgument) = {
    copy(Seq(in))
  }
  
  def apply(in: String) = {
    copy(Seq(TexValue(in)))
  }

  def apply(in: TexCommandInline) = {
    copy(TexValue("\\" + in.name) +: in.args.sequence)
  }

  protected def copy(append: Seq[AbstractTexObject]) = {
    new TexEnvironment(sequence.append(append)) {
      override def name() = TexEnvironment.this.name()
    }
  }

}