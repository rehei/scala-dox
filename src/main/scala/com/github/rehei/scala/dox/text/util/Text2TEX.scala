package com.github.rehei.scala.dox.text.util

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObject
import com.github.rehei.scala.dox.text.TextObjectArrowRight
import com.github.rehei.scala.dox.text.TextObjectArrowUp
import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.text.TextObjectDoubleStruck
import com.github.rehei.scala.dox.text.TextObjectItalic
import com.github.rehei.scala.dox.text.TextObjectLetterDeltaLowercase
import com.github.rehei.scala.dox.text.TextObjectLetterDeltaUppercase
import com.github.rehei.scala.dox.text.TextObjectLetterEpsilonLowercase
import com.github.rehei.scala.dox.text.TextObjectLetterTauLowercase
import com.github.rehei.scala.dox.text.TextObjectNewline
import com.github.rehei.scala.dox.text.TextObjectSubscript
import com.github.rehei.scala.dox.text.TextObjectTab
import com.github.rehei.scala.dox.text.TextObjectLetterLambdaLowercase
import com.github.rehei.scala.dox.text.TextObjectLetterZetaLowercase

object Text2TEX extends Text2TEX(false) {

  protected trait MathSensitiveParsing {
    def mathEnvironment(text: String): String
    def subscript(text: String): String
    def newline: String
  }
}

case class Text2TEX protected (isMathMode: Boolean) {

  import com.github.rehei.scala.dox.control.tex.TexEscape._

  case class InMathMode() extends Text2TEX.MathSensitiveParsing {

    def mathEnvironment(text: String) = { text }
    def subscript(text: String) = "_{" + text + "}"
    def newline = "\\\\"

  }

  case class InTextMode() extends Text2TEX.MathSensitiveParsing {
    def mathEnvironment(text: String) = "$" + text + "$"
    def subscript(text: String) = "\\textsubscript{" + text + "}"
    def newline = "\\newline{}"
  }

  protected val mode = {
    if (isMathMode) {
      InMathMode()
    } else
      InTextMode()
  }

  case class ParseResult(protected val text: String, protected val size: Int) {

    protected val all = ArrayBuffer[ParseResult]()

    def append(result: ParseResult) {
      all.append(result)
    }

    def totalText = {
      all.map(_.text).mkString
    }

    def totalCount = {
      all.map(_.size).sum
    }

  }

  object SpecialSignParser {
    val all = ArrayBuffer[SpecialSignParser[_]]()
  }

  case class SpecialSignParser[T <: TextObject](tex: String)(implicit val classTag: ClassTag[T]) {

    SpecialSignParser.all.append(this)

    def parse(sequence: Seq[TextObject]) = {
      val collection = collect[T](sequence)
      val result = parseExplicit(collection, 0)
      ParseResult(result, collection.size)
    }

    protected def parseExplicit(data: Seq[T], index: Int): String = {
      data.lift(index).map {
        _ => s"${tex}" + parseExplicit(data, index + 1)
      } getOrElse {
        ""
      }
    }
  }

  SpecialSignParser[TextObjectNewline](mode.newline)
  SpecialSignParser[TextObjectArrowRight](mode.mathEnvironment("\\rightarrow"))
  SpecialSignParser[TextObjectArrowUp](mode.mathEnvironment("\\uparrow"))
  SpecialSignParser[TextObjectLetterDeltaLowercase](mode.mathEnvironment("\\delta{}"))
  SpecialSignParser[TextObjectLetterDeltaUppercase](mode.mathEnvironment("\\Delta{}"))
  SpecialSignParser[TextObjectLetterEpsilonLowercase](mode.mathEnvironment("\\epsilon{}"))
  SpecialSignParser[TextObjectLetterTauLowercase](mode.mathEnvironment("\\tau{}"))
  SpecialSignParser[TextObjectLetterLambdaLowercase](mode.mathEnvironment("\\lambda{}"))
  SpecialSignParser[TextObjectLetterZetaLowercase](mode.mathEnvironment("\\zeta{}"))

  def generate(element: TextAST) = {

    val base = ParseResult("", 0)
    asText(base, element.sequence)
  }

  protected def asText(base: ParseResult, sequence: Seq[TextObject]) = {

    def next() = {
      sequence.drop(base.totalCount)
    }

    while (base.totalCount < sequence.size) {

      val before = base.totalCount

      base.append(textDefault(next()))
      base.append(textSubscript(next()))
      base.append(textItalic(next()))
      base.append(textDoubleStruck(next()))
      base.append(textTab(next()))

      for (parser <- SpecialSignParser.all) {
        base.append(parser.parse(next()))
      }

      if (base.totalCount == before) {
        throw new IllegalArgumentException("TextObject type not supported: " + next().head.getClass.getSimpleName)
      }

    }

    base.totalText
  }

  protected def textDefault(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectDefault](sequence)
    val resultString = collection.map(text => escape(text.in)).mkString

    ParseResult(resultString, collection.size)
  }

  protected def textTab(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectTab](sequence)
    val resultString = collection.map(text => "\\hspace{5mm}").mkString

    ParseResult(resultString, collection.size)
  }

  protected def textItalic(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectItalic](sequence)
    val resultString = collection.map(text => "\\textit{" + text.in + "}").mkString

    ParseResult(resultString, collection.size)
  }

  protected def textDoubleStruck(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectDoubleStruck](sequence)
    val resultString = collection.map(text => mode.mathEnvironment("\\mathbb{" + text.in + "}")).mkString

    ParseResult(resultString, collection.size)
  }

  protected def textSubscript(sequence: Seq[TextObject]): ParseResult = {
    val collection = collect[TextObjectSubscript](sequence)
    if (collection.isEmpty) {
      ParseResult("", 0)
    } else {
      val mergedSubscriptArgs = collection.map(text => Text2TEX(isMathMode).generate(text.textAST)).mkString
      val resultString = mode.subscript(mergedSubscriptArgs)
      ParseResult(resultString, collection.size)
    }
  }

  protected def collect[T](sequence: Seq[TextObject])(implicit classTag: ClassTag[T]) = {
    val subSequence = sequence.takeWhile(classTag.runtimeClass.isInstance(_))
    subSequence.map(_.asInstanceOf[T])
  }

}