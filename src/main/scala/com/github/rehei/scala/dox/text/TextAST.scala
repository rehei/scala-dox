package com.github.rehei.scala.dox.text

import scala.xml.NodeSeq

case class TextAST(val sequence: Seq[TextObject]) {

  def append(in: TextAST) = {
    this.copy(sequence = sequence ++ in.sequence)
  }

  def space() = {
    text(" ")
  }
  
  def spaceSmall() = {
    this.copy(sequence = sequence :+ TextObjectSpaceSmall())
  }

  def text(in: String) = {
    this.copy(sequence = sequence :+ TextObjectDefault(in))
  }

  def subscript(in: String): TextAST = {
    this.subscript(TextFactory.text(in))
  }

  def subscript(in: TextAST): TextAST = {
    this.copy(sequence = sequence :+ TextObjectSubscript(in))
  }

  def italic(in: String) = {
    this.copy(sequence = sequence :+ TextObjectItalic(in))
  }

  def newline() = {
    this.copy(sequence = sequence :+ TextObjectNewline())
  }

  def tab() = {
    this.copy(sequence = sequence :+ TextObjectTab())
  }

  def arrowRight() = {
    this.copy(sequence = sequence :+ TextObjectArrowRight())
  }

  def arrowUp() = {
    this.copy(sequence = sequence :+ TextObjectArrowUp())
  }

  def rule() = {
    this.copy(sequence = sequence :+ TextObjectRule())
  }

  def doubleStruck(in: Char) = {
    this.copy(sequence = sequence :+ TextObjectDoubleStruck(in))
  }

  def greek(letterCallback: TextObjectGreekLetter.type => TextObjectGreekLetter, caseCallback: TextObjectCase.type => TextObjectCase) = {
    val effectiveLetter = letterCallback(TextObjectGreekLetter)
    val effectiveCase = caseCallback(TextObjectCase)
    
    this.copy(sequence = sequence :+ TextObjectGreekLetterWithCase(effectiveLetter, effectiveCase))
  }
  
}