package com.github.rehei.scala.dox.text

import scala.xml.NodeSeq

case class TextAST(val sequence: Seq[TextObject]) {

  def append(in: TextAST) = {
    this.copy(sequence = sequence ++ in.sequence)
  }

  def text(in: String) = {
    this.copy(sequence = sequence :+ TextObjectDefault(in))
  }

  def subscript(in: String) = {
    this.copy(sequence = sequence :+ TextObjectSubscript(sequence.reverse.takeWhile(_.isInstanceOf[TextObjectSubscript]).size, in))
  }

}