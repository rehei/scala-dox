package com.github.rehei.scala.dox.text.util

import scala.reflect.ClassTag

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObject
import com.github.rehei.scala.dox.text.TextObjectArrowRight
import com.github.rehei.scala.dox.text.TextObjectArrowUp
import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.text.TextObjectLetterDeltaUppercase
import com.github.rehei.scala.dox.text.TextObjectLetterEpsilonLowercase
import com.github.rehei.scala.dox.text.TextObjectNewline
import com.github.rehei.scala.dox.text.TextObjectSubscript
import com.github.rehei.scala.dox.text.TextObjectLetterTauLowercase
import com.github.rehei.scala.dox.text.TextObjectLetterDeltaLowercase
import scala.collection.mutable.ArrayBuffer

object Text2TEX {

  import com.github.rehei.scala.dox.control.tex.TexEscape._

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

  SpecialSignParser[TextObjectNewline]("\\newline{}")
  SpecialSignParser[TextObjectArrowRight]("$\\rightarrow$")
  SpecialSignParser[TextObjectArrowUp]("$\\uparrow$")
  SpecialSignParser[TextObjectLetterDeltaLowercase]("$\\delta{}$")
  SpecialSignParser[TextObjectLetterDeltaUppercase]("$\\Delta{}$")
  SpecialSignParser[TextObjectLetterEpsilonLowercase]("$\\epsilon{}$")
  SpecialSignParser[TextObjectLetterTauLowercase]("$\\tau{}$")

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

      
      for (parser <- SpecialSignParser.all) {
        base.append(parser.parse(next()))
      }

      println(base.totalText)
      println(base.totalCount)
      
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

  protected def textSubscript(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectSubscript](sequence)
    val result = textSubScriptExplicit(collection, 0)

    ParseResult(result, collection.size)
  }

  protected def textSubScriptExplicit(subscriptSeq: Seq[TextObjectSubscript], index: Int): String = {
    subscriptSeq.lift(index).map {
      text => "\\textsubscript{" + Text2TEX.generate(text.textAST) + "}" + textSubScriptExplicit(subscriptSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def collect[T](sequence: Seq[TextObject])(implicit classTag: ClassTag[T]) = {
    val subSequence = sequence.takeWhile(classTag.runtimeClass.isInstance(_))
    subSequence.map(_.asInstanceOf[T])
  }

}