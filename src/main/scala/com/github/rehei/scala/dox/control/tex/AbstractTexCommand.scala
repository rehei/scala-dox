package com.github.rehei.scala.dox.control.tex

import scala.inline

abstract class AbstractTexCommand[T <: AbstractTexCommand[_]](inline: Boolean, args: TexSeq) extends AbstractTexObject {

  def name(): String

  def apply(in: String): T = {
    this.create(TexSeq(Seq(TexValue(in))))
  }

  def apply(value: TexValue): T = {
    this.create(TexSeq(Seq(value)))
  }

  def apply(option: TexOption) = {
    this.create(TexSeq(Seq(option)))
  }

  def apply(in: TexSeq): T = {
    this.create(in)
  }

  def generate() = {
    "\\" + name + args.generate() + extension()
  }

  protected def extension() = {
    if (inline) { "" } else { "\n" }
  }

  protected def create(in: TexSeq): T

}