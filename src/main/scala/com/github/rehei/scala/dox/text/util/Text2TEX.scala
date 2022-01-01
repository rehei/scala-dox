package com.github.rehei.scala.dox.text.util

import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextObjectSubscript
import com.github.rehei.scala.dox.text.TextObject
import com.github.rehei.scala.dox.text.TextObjectDefault
import com.github.rehei.scala.dox.control.tex.TexEscape
import scala.reflect.ClassTag
import scala.collection.mutable.ArrayBuffer
import com.github.rehei.scala.dox.text.TextObjectNewline
import com.github.rehei.scala.dox.text.TextObjectArrowRight
import com.github.rehei.scala.dox.text.TextObjectDeltaUppercase

object Text2TEX {

  case class ParseResult(var text: String, var count: Int) {

    def append(result: ParseResult) {
      text = text + result.text
      count = count + result.count
    }

  }

  import TexEscape._

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
      base.append(textDelta(sequence.drop(base.count)))

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

  protected def textDelta(sequence: Seq[TextObject]) = {
    val collection = collect[TextObjectDeltaUppercase](sequence)
    val result = textDeltaExplicit(collection, 0)
    ParseResult(result, collection.size)
  }

  protected def textSubScriptExplicit(subscriptSeq: Seq[TextObjectSubscript], index: Int): String = {
    subscriptSeq.lift(index).map {
      text => "\\textsubscript{" + escape(text.in) + textSubScriptExplicit(subscriptSeq, index + 1) + "}"
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

  protected def textDeltaExplicit(newlineSeq: Seq[TextObjectDeltaUppercase], index: Int): String = {
    newlineSeq.lift(index).map {
      newline => " $\\Delta$" + textDeltaExplicit(newlineSeq, index + 1)
    } getOrElse {
      ""
    }
  }

  protected def collect[T](sequence: Seq[TextObject])(implicit classTag: ClassTag[T]) = {
    val subSequence = sequence.takeWhile(classTag.runtimeClass.isInstance(_))
    subSequence.map(_.asInstanceOf[T])
  }

}