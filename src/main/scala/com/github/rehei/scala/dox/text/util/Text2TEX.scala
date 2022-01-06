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

object Text2TEX {

  case class ParseResult(var text: String, var count: Int) {

    def append(result: ParseResult) {
      text = text + result.text
      count = count + result.count
    }

  }

  import com.github.rehei.scala.dox.control.tex.TexEscape._

  def generate(element: TextAST) = {

    val base = ParseResult("", 0)
    asText(base, element.sequence)
  }

  protected def asText(base: ParseResult, sequence: Seq[TextObject]) = {

    while (base.count < sequence.size) {

      val before = base.count

      base.append(textDefault(sequence.drop(base.count)))
      base.append(textSubscript(sequence.drop(base.count)))
      base.append(textNewline(sequence.drop(base.count)))
      base.append(textArrowRight(sequence.drop(base.count)))
      base.append(textArrowUp(sequence.drop(base.count)))
      base.append(textLetterDelta(sequence.drop(base.count)))
      base.append(textLetterEpsilonLower(sequence.drop(base.count)))
      base.append(textLetterTauLower(sequence.drop(base.count)))

      if (base.count == before) {
        throw new IllegalArgumentException("TextObject type not supported: " + sequence.drop(base.count).head.getClass.getSimpleName)
      }

    }

    base.text

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

  protected def textNewline(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectNewline](sequence)
    val result = textNewlineExplicit(collection, 0)
    ParseResult(result, collection.size)
  }

  protected def textArrowRight(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectArrowRight](sequence)
    val result = textArrowRightExplicit(collection, 0)
    ParseResult(result, collection.size)
  }

  protected def textArrowUp(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectArrowUp](sequence)
    val result = textArrowUpExplicit(collection, 0)
    ParseResult(result, collection.size)
  }

  protected def textLetterDelta(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectLetterDeltaUppercase](sequence)
    val result = textLetterDeltaExplicit(collection, 0)
    ParseResult(result, collection.size)
  }

  protected def textLetterEpsilonLower(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectLetterEpsilonLowercase](sequence)
    val result = textLetterEpsilonLowerExplicit(collection, 0)
    ParseResult(result, collection.size)
  }

  protected def textLetterTauLower(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectLetterTauLowercase](sequence)
    val result = textLetterTauLowerExplicit(collection, 0)
    ParseResult(result, collection.size)
  }

  protected def textSubScriptExplicit(subscriptSeq: Seq[TextObjectSubscript], index: Int): String = {
    subscriptSeq.lift(index).map {
      text => "\\textsubscript{" + Text2TEX.generate(text.textAST) + "}" + textSubScriptExplicit(subscriptSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def textNewlineExplicit(newlineSeq: Seq[TextObjectNewline], index: Int): String = {
    newlineSeq.lift(index).map {
      newline => " \\newline " + textNewlineExplicit(newlineSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def textArrowRightExplicit(newlineSeq: Seq[TextObjectArrowRight], index: Int): String = {
    newlineSeq.lift(index).map {
      newline => " $\\rightarrow$ " + textArrowRightExplicit(newlineSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def textArrowUpExplicit(newlineSeq: Seq[TextObjectArrowUp], index: Int): String = {
    newlineSeq.lift(index).map {
      newline => " $\\uparrow$ " + textArrowUpExplicit(newlineSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def textLetterDeltaExplicit(newlineSeq: Seq[TextObjectLetterDeltaUppercase], index: Int): String = {
    newlineSeq.lift(index).map {
      newline => "$\\Delta$" + textLetterDeltaExplicit(newlineSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def textLetterEpsilonLowerExplicit(newlineSeq: Seq[TextObjectLetterEpsilonLowercase], index: Int): String = {
    newlineSeq.lift(index).map {
      newline => "$\\epsilon$" + textLetterEpsilonLowerExplicit(newlineSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def textLetterTauLowerExplicit(newlineSeq: Seq[TextObjectLetterTauLowercase], index: Int): String = {
    newlineSeq.lift(index).map {
      newline => "$\\tau$" + textLetterTauLowerExplicit(newlineSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def collect[T](sequence: Seq[TextObject])(implicit classTag: ClassTag[T]) = {
    val subSequence = sequence.takeWhile(classTag.runtimeClass.isInstance(_))
    subSequence.map(_.asInstanceOf[T])
  }

}