package com.github.rehei.scala.dox.text

import scala.xml.NodeSeq

case class TextAST(val sequence: Seq[TextObject]) {

  def text(in: String) = {
    this.copy(sequence = sequence :+ TextObjectDefault(in))
  }

  def subscript(in: String) = {
    this.copy(sequence = sequence :+ TextObjectSubscript(sequence.reverse.takeWhile(_.isInstanceOf[TextObjectSubscript]).size, in))
  }

  def indented(in: String, space: Int) = {
    this.copy(sequence = sequence :+ TextObjectSpace(in, space))
  }

  def isEmpty() = !sequence.exists(value => !value.isEmpty)
}