package com.github.rehei.scala.dox.text

import scala.xml.NodeSeq
import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.text.util.Text2TEX

case class TextAST(sequence: Seq[TextObject]) {

  def asPlainTex() = {
    Text2TEX.generate(this)
  }

  def asPlainTexInMath() = {
    Text2TEX(true).generate(this)
  }

  def append(in: TextAST) = {
    this.copy(sequence = sequence ++ in.sequence)
  }

  def extend(value: String) = {
    space().text("(").text(value).text(")")
  }

  def space() = {
    text(" ")
  }

  def spaceSVG(offset: Double) = {
    this.copy(sequence = sequence :+ TextObjectSpaceSVG(offset))
  }

  def math(in: TextAST) = {
    this.copy(sequence = sequence :+ TextObjectMath(in))
  }

  def plain(in: String) = {
    this.copy(sequence = sequence :+ TextObjectPlain(in))
  }

  def spaceSmall() = {
    this.copy(sequence = sequence :+ TextObjectSpaceSmall())
  }

  def text(in: String) = {
    this.copy(sequence = sequence :+ TextObjectDefault(in))
  }

  def parbox(cm: Double, content: String): TextAST = {
    this.copy(sequence = sequence :+ TextObjectParbox(cm, content))
  }

  def subscript(in: String): TextAST = {
    this.subscript(TextFactory.text(in))
  }

  def subscript(in: TextAST): TextAST = {
    this.copy(sequence = sequence :+ TextObjectSubscript(in))
  }

  def overline(in: TextAST) = {
    this.copy(sequence = sequence :+ TextObjectDecorateOverline(in))
  }

  def underline(in: TextAST) = {
    this.copy(sequence = sequence :+ TextObjectDecorateUnderline(in))
  }

  def italic(in: TextAST) = {
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

  def rule() = {
    this.copy(sequence = sequence :+ TextObjectRule())
  }

  def cite(value: String) = {
    this.copy(sequence = sequence :+ TextObjectCite(value))
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