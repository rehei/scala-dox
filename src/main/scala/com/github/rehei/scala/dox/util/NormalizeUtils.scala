package com.github.rehei.scala.dox.util

import scala.collection.mutable.ArrayBuffer
import java.util.regex.Pattern.quote

object NormalizeUtils {

  case class TexPattern(matching: String, replacement: String)

  protected val texPatternSeq = ArrayBuffer[TexPattern]()

  protected val none = ""
  protected val azAZ = "[a-zA-Z]"
  protected val $1 = "$1"

  \ { "`" } { azAZ } replaceWith $1
  \ { "'" } { azAZ } replaceWith $1
  \ { quote("^") } { azAZ } replaceWith $1
  \ { '"'.toString() } { azAZ } replaceWith $1
  \ { "H" } { azAZ } replaceWith $1
  \ { "~" } { azAZ } replaceWith $1
  \ { "c" } { azAZ } replaceWith $1
  \ { "k" } { azAZ } replaceWith $1
  \ { "l" } { none } replaceWith "l"
  \ { "=" } { azAZ } replaceWith $1
  \ { "b" } { azAZ } replaceWith $1
  \ { quote(".") } { azAZ } replaceWith $1
  \ { "d" } { azAZ } replaceWith $1
  \ { "r" } { azAZ } replaceWith $1
  \ { "u" } { azAZ } replaceWith $1
  \ { "v" } { azAZ } replaceWith $1
  \ { "o" } { none } replaceWith "ø"
  \ { "" } { quote("\\i") } replaceWith "ı"
  \ { "ss" } { none } replaceWith "ß"

  def normalizeBibTex(input: String) = {
    var result = input
    for (pattern <- texPatternSeq) {
      result = result.replaceAll(pattern.matching, pattern.replacement)
    }
    result
  }

  def normalizeString(input: String) = {
    org.apache.commons.lang3.StringUtils.stripAccents(input)
  }

  protected def \(texCommand: String)(argument: String) = new {
    def replaceWith(replacement: String) = {
      val > = quote("}")
      val < = quote("{")
      val matching = < + "\\\\" + texCommand + < + "(" + argument + ")" + > + >
      val pattern = TexPattern(matching, replacement)
      texPatternSeq.append(pattern)
    }
  }

  def trim(value: String) = {
    value.replace('\u00a0', ' ').trim() // replace non breaking space and trim
  }

}