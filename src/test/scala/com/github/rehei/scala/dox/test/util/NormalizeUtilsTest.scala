package com.github.rehei.scala.dox.test.util

import org.junit.Test
import org.jbibtex.StringUtil
import com.github.rehei.scala.dox.util.IOUtils
import com.github.rehei.scala.dox.util.NormalizeUtils
import org.junit.Assert

class NormalizeUtilsTest {

  case class $(scala: String, tex: String, normalized: String)

  @Test
  def test() {
    val testBase = Seq(
      $("ò", "\\`{o}", "o"),
      $("ó", "\\'{o}", "o"),
      $("ô", "\\^{o}", "o"),
      $("ö", "\\\"{o}", "o"),
      $("ő", "\\H{o}", "o"),
      $("õ", "\\~{o}", "o"),
      $("ç", "\\c{c}", "c"),
      $("ą", "\\k{a}", "a"),
      $("ł", "\\l{}", "l"),
      $("ō", "\\={o}", "o"),
      $("o", "\\b{o}", "o"),
      $("ȯ", "\\.{o}", "o"),
      $("ụ", "\\d{u}", "u"),
      $("å", "\\r{a}", "a"),
      $("ŏ", "\\u{o}", "o"),
      $("š", "\\v{s}", "s"),
      $("ø", "\\o{}", "ø"),
      $("ı", "\\{\\i}", "ı"),
      $("ß", "\\ss{}", "ß"))

    val givenScala = testBase.map(_.scala).mkString
    val givenTex = testBase.map(_.tex).mkString
    val expected = testBase.map(_.normalized).mkString

    Assert.assertEquals(expected, NormalizeUtils.normalizeString(givenScala))
    Assert.assertEquals(expected, NormalizeUtils.normalizeTex(givenTex))
  }

}