package com.github.rehei.scala.dox.control.tex

case class TexArgument(protected val argument: String) extends AbstractTexArgument(argument) {
  override def generate() = {
    "(" + argument + ")"
  }
}