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

  def newline() = {
    this.copy(sequence = sequence :+ TextObjectNewline())
  }

  def arrowRight() = {
    this.copy(sequence = sequence :+ TextObjectArrowRight())
  }

  def arrowUp() = {
    this.copy(sequence = sequence :+ TextObjectArrowUp())
  }

  def letterVUppercase(in: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruckV(in))
  }

  def letterSUppercase(in: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruckS(in))
  }

  def letterTUppercase(in: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruckT(in))
  }

  def letterFUppercase(in: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruckF(in))
  }

  def letterIUppercase(in: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruckI(in))
  }

  def letterWUppercase(in: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruckW(in))
  }

  def letterGUppercase(in: Option[String]) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruckG(in))
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