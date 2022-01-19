package com.github.rehei.scala.dox.text

import scala.xml.NodeSeq

case class TextAST(val sequence: Seq[TextObject]) {

  def append(in: TextAST) = {
    this.copy(sequence = sequence ++ in.sequence)
  }

  def text(in: String) = {
    this.copy(sequence = sequence :+ TextObjectDefault(in))
  }

  def subscript(in: String): TextAST = {
    this.subscript(TextFactory.text(in))
  }

  def subscript(in: TextAST): TextAST = {
    this.copy(sequence = sequence :+ TextObjectSubscript(sequence.reverse.takeWhile(_.isInstanceOf[TextObjectSubscript]).size, in))
  }

  def italic(in: String) = {
    this.copy(sequence = sequence :+ TextObjectItalic(in))
  }

  def newline() = {
    this.copy(sequence = sequence :+ TextObjectNewline())
  }

  def arrowRight() = {
    this.copy(sequence = sequence :+ TextObjectArrowRight())
  }

  def arrowUp() = {
    this.copy(sequence = sequence :+ TextObjectArrowUp())
  }

  def space() = {
    this.copy(sequence = sequence :+ TextObjectSpace())
  }

  def rule() = {
    this.copy(sequence = sequence :+ TextObjectRule())
  }

  def doubleStruck(in: Char, subscript: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruck(in, subscript))
  }

  def letterDeltaUppercase() = {
    this.copy(sequence = sequence :+ TextObjectLetterDeltaUppercase())
  }

  def letterDeltaLowercase() = {
    this.copy(sequence = sequence :+ TextObjectLetterDeltaLowercase())
  }

  def letterEpsilonLowercase() = {
    this.copy(sequence = sequence :+ TextObjectLetterEpsilonLowercase())
  }

  def letterTauLowercase() = {
    this.copy(sequence = sequence :+ TextObjectLetterTauLowercase())
  }
}