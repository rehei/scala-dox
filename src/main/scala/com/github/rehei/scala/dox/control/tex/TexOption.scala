package com.github.rehei.scala.dox.control.tex

case class TexOption(protected val argument: String) extends AbstractTexArgument(argument) {
  override def generate() = {
    "[" + argument + "]"
  }
}
