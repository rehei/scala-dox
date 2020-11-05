package com.github.rehei.scala.dox.control.tex

abstract class AbstractTexArgument(argument: String) extends AbstractTexObject {
  def generate() = argument

  def apply(in: Int) = {
    this.create(in.toString())
  }

  def apply(in: String) = {
    this.create(in)
  }

  protected def create(in: String) = {
    TexSeq(Seq(this, TexValue(in)))
  }
}