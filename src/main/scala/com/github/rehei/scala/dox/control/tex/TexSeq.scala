package com.github.rehei.scala.dox.control.tex

case class TexSeq(sequence: Seq[AbstractTexObject]) extends AbstractTexObject {
  def apply(in: String) = {
    TexSeq(sequence :+ TexValue(in))
  }
  def apply(in: AbstractTexArgument) = {
    TexSeq(sequence :+ in)
  }
  def apply(extension: TexSeq) = {
    TexSeq(sequence ++ extension.sequence)
  }
  def generate() = {
    sequence.map(sub(_)).mkString
  }

  def sub(in: AbstractTexObject) = {
    in match {
      case element @ TexOption(_) => "[" + element.generate() + "]"
      case element                => "{" + element.generate() + "}"
    }
  }
}