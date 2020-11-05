package com.github.rehei.scala.dox.control.tex

abstract class AbstractTexCommand[T <: AbstractTexCommand[_]](name: String, args: TexSeq) extends AbstractTexObject {

  def apply(in: String): T = {
    this.create(TexSeq(Seq(TexValue(in))))
  }

  def apply(in: AbstractTexArgument): T = {
    this.create(TexSeq(Seq(in)))
  }

  def apply(in: TexSeq): T = {
    this.create(in)
  }

  def generate() = {
    "\\" + name + args.generate()
  }

  protected def create(in: TexSeq): T

}