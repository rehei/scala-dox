package com.github.rehei.scala.dox.control.tex

class TexEnvironmentNamed(_name: String, sequence: TexSeq) extends TexEnvironment(sequence) {
  override def name() = _name
}
