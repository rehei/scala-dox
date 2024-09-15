package com.github.rehei.scala.dox.text.util

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObject
import com.github.rehei.scala.dox.text.TextObjectArrowRight
import com.github.rehei.scala.dox.text.TextObjectCase
import com.github.rehei.scala.dox.text.TextObjectCite
import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.text.TextObjectDoubleStruck
import com.github.rehei.scala.dox.text.TextObjectGreekLetter
import com.github.rehei.scala.dox.text.TextObjectGreekLetterWithCase
import com.github.rehei.scala.dox.text.TextObjectItalic
import com.github.rehei.scala.dox.text.TextObjectNewline
import com.github.rehei.scala.dox.text.TextObjectSpaceSmall
import com.github.rehei.scala.dox.text.TextObjectSubscript
import com.github.rehei.scala.dox.text.TextObjectTab
import com.github.rehei.scala.dox.text.TextObjectParbox
import com.github.rehei.scala.dox.text.TextObjectPlain
import com.github.rehei.scala.dox.text.TextObjectMath
import com.github.rehei.scala.dox.text.TextObjectOverline

object Text2TEX extends Text2TEX(false) {

  protected trait MathSensitiveParsing {
    def mathEnvironment(text: String): String
    def subscript(text: String): String
    def superscript(text: String): String
    def newline: String
  }
}

case class Text2TEX protected (isMathMode: Boolean) {

  import com.github.rehei.scala.dox.control.tex.TexEscape._

  case class InMathMode() extends Text2TEX.MathSensitiveParsing {

    def mathEnvironment(text: String) = { text }
    def subscript(text: String) = "_{" + text + "}"
    def superscript(text: String) = "^{" + text + "}"
    def newline = "\\\\"

  }

  case class InTextMode() extends Text2TEX.MathSensitiveParsing {
    def mathEnvironment(text: String) = "$" + text + "$"
    def subscript(text: String) = "\\textsubscript{" + text + "}"
    def superscript(text: String) = "^{" + text + "}"
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

  SpecialSignParser[TextObjectSpaceSmall]("\\,")
  SpecialSignParser[TextObjectNewline](mode.newline)
  SpecialSignParser[TextObjectArrowRight]("$\\relbar\\joinrel\\mathrel{\\vcenter{\\hbox{\\scalebox{0.75}{$\\RHD$}}}}$")

  protected val GREEK_LOOKUP = {
    Map(
      (greek(_.ALPHA, _.UPPERCASE), "A"),
      (greek(_.ALPHA, _.LOWERCASE), mode.mathEnvironment("\\alpha{}")),

      (greek(_.BETA, _.UPPERCASE), "B"),
      (greek(_.BETA, _.LOWERCASE), mode.mathEnvironment("\\beta{}")),

      (greek(_.GAMMA, _.UPPERCASE), mode.mathEnvironment("\\Gamma{}")),
      (greek(_.GAMMA, _.LOWERCASE), mode.mathEnvironment("\\gamma{}")),

      (greek(_.DELTA, _.UPPERCASE), mode.mathEnvironment("\\Delta{}")),
      (greek(_.DELTA, _.LOWERCASE), mode.mathEnvironment("\\delta{}")),

      (greek(_.EPSILON, _.UPPERCASE), "E"),
      (greek(_.EPSILON, _.LOWERCASE), mode.mathEnvironment("\\epsilon{}")),
      (greek(_.EPSILON, _.VARIANT), mode.mathEnvironment("\\varepsilon{}")),

      (greek(_.ZETA, _.UPPERCASE), "Z"),
      (greek(_.ZETA, _.LOWERCASE), mode.mathEnvironment("\\zeta{}")),

      (greek(_.ETA, _.UPPERCASE), "H"),
      (greek(_.ETA, _.LOWERCASE), mode.mathEnvironment("\\eta{}")),

      (greek(_.THETA, _.UPPERCASE), mode.mathEnvironment("\\Theta{}")),
      (greek(_.THETA, _.LOWERCASE), mode.mathEnvironment("\\theta{}")),
      (greek(_.THETA, _.VARIANT), mode.mathEnvironment("\\vartheta{}")),

      (greek(_.IOTA, _.UPPERCASE), "I"),
      (greek(_.IOTA, _.LOWERCASE), mode.mathEnvironment("\\iota{}")),

      (greek(_.KAPPA, _.UPPERCASE), "K"),
      (greek(_.KAPPA, _.LOWERCASE), mode.mathEnvironment("\\kappa{}")),

      (greek(_.LAMBDA, _.UPPERCASE), mode.mathEnvironment("\\Lambda{}")),
      (greek(_.LAMBDA, _.LOWERCASE), mode.mathEnvironment("\\lambda{}")),

      (greek(_.MU, _.UPPERCASE), "M"),
      (greek(_.MU, _.LOWERCASE), mode.mathEnvironment("\\mu{}")),

      (greek(_.NU, _.UPPERCASE), "N"),
      (greek(_.NU, _.LOWERCASE), mode.mathEnvironment("\\nu{}")),

      (greek(_.XI, _.UPPERCASE), mode.mathEnvironment("\\Xi{}")),
      (greek(_.XI, _.LOWERCASE), mode.mathEnvironment("\\xi{}")),

      (greek(_.OMICRON, _.UPPERCASE), "O"),
      (greek(_.OMICRON, _.LOWERCASE), "o"),

      (greek(_.PI, _.UPPERCASE), mode.mathEnvironment("\\Pi{}")),
      (greek(_.PI, _.LOWERCASE), mode.mathEnvironment("\\pi{}")),

      (greek(_.RHO, _.UPPERCASE), "P"),
      (greek(_.RHO, _.LOWERCASE), mode.mathEnvironment("\\rho{}")),
      (greek(_.RHO, _.VARIANT), mode.mathEnvironment("\\varrho{}")),

      (greek(_.SIGMA, _.UPPERCASE), mode.mathEnvironment("\\Sigma{}")),
      (greek(_.SIGMA, _.LOWERCASE), mode.mathEnvironment("\\sigma{}")),

      (greek(_.TAU, _.UPPERCASE), "T"),
      (greek(_.TAU, _.LOWERCASE), mode.mathEnvironment("\\tau{}")),

      (greek(_.UPSILON, _.UPPERCASE), mode.mathEnvironment("\\Upsilon{}")),
      (greek(_.UPSILON, _.LOWERCASE), mode.mathEnvironment("\\upsilon{}")),

      (greek(_.PHI, _.UPPERCASE), mode.mathEnvironment("\\Phi{}")),
      (greek(_.PHI, _.LOWERCASE), mode.mathEnvironment("\\phi{}")),
      (greek(_.PHI, _.VARIANT), mode.mathEnvironment("\\varphi{}")),

      (greek(_.CHI, _.UPPERCASE), "X"),
      (greek(_.CHI, _.LOWERCASE), mode.mathEnvironment("\\chi{}")),

      (greek(_.PSI, _.UPPERCASE), mode.mathEnvironment("\\Psi{}")),
      (greek(_.PSI, _.LOWERCASE), mode.mathEnvironment("\\psi{}")),

      (greek(_.OMEGA, _.UPPERCASE), mode.mathEnvironment("\\Omega{}")),
      (greek(_.OMEGA, _.LOWERCASE), mode.mathEnvironment("\\omega{}")))
  }

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
      base.append(textOverline(next()))
      base.append(textMath(next()))
      base.append(textParbox(next()))
      base.append(textDoubleStruck(next()))
      base.append(textTab(next()))
      base.append(textGreek(next()))
      base.append(textCite(next()))
      base.append(textPlain(next()))

      for (parser <- SpecialSignParser.all) {
        base.append(parser.parse(next()))
      }

      if (base.totalCount == before) {
        throw new IllegalArgumentException("TextObject type not supported: " + next().head.getClass.getSimpleName)
      }

    }

    base.totalText
  }

  protected def textCite(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectCite](sequence)
    val resultString = collection.map(text => "\\cite{" + text.in + "}").mkString(", ")

    ParseResult(resultString, collection.size)
  }

  protected def textGreek(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectGreekLetterWithCase](sequence)
    val resultString = collection.map(GREEK_LOOKUP.get(_).get).mkString

    ParseResult(resultString, collection.size)
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

  protected def textItalic(sequence: Seq[TextObject]): ParseResult = {
    val collection = collect[TextObjectItalic](sequence)
    val resultString = collection.map(text => "\\textit{" + Text2TEX(false).generate(text.in) + "}").mkString

    ParseResult(resultString, collection.size)
  }

  protected def textOverline(sequence: Seq[TextObject]): ParseResult = {
    val collection = collect[TextObjectOverline](sequence)
    val resultString = collection.map(text => mode.mathEnvironment("\\overline{\\vphantom{\\text{\\small{A}}}" + Text2TEX(true).generate(text.in) + "}")).mkString

    ParseResult(resultString, collection.size)
  }

  protected def textMath(sequence: Seq[TextObject]): ParseResult = {
    val collection = collect[TextObjectMath](sequence)

    val resultString = {
      if (isMathMode) {
        collection.map(text => Text2TEX(true).generate(text.in)).mkString
      } else {
        collection.map(text => "$" + Text2TEX(true).generate(text.in) + "$").mkString
      }
    }

    ParseResult(resultString, collection.size)
  }

  protected def textParbox(sequence: Seq[TextObject]): ParseResult = {
    val collection = collect[TextObjectParbox](sequence)
    val resultString = collection.map(box => "\\parbox{" + box.cm + "cm}{" + box.content + "}").mkString

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
      val args = collection.map(text => Text2TEX(isMathMode).generate(text.textAST)).mkString
      val resultString = mode.subscript(args)
      ParseResult(resultString, collection.size)
    }
  }

  protected def textPlain(sequence: Seq[TextObject]): ParseResult = {
    val collection = collect[TextObjectPlain](sequence)
    val resultString = collection.map(_.in).mkString

    ParseResult(resultString, collection.size)
  }

  protected def collect[T](sequence: Seq[TextObject])(implicit classTag: ClassTag[T]) = {
    val subSequence = sequence.takeWhile(classTag.runtimeClass.isInstance(_))
    subSequence.map(_.asInstanceOf[T])
  }

  def greek(letterCallback: TextObjectGreekLetter.type => TextObjectGreekLetter, caseCallback: TextObjectCase.type => TextObjectCase) = {
    val effectiveLetter = letterCallback(TextObjectGreekLetter)
    val effectiveCase = caseCallback(TextObjectCase)

    TextObjectGreekLetterWithCase(effectiveLetter, effectiveCase)
  }

}