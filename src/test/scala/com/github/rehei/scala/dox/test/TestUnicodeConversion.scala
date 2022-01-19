package com.github.rehei.scala.dox.test

import org.junit.Test
import org.apache.commons.lang3.StringEscapeUtils

class TestUnicodeConversion {
  
  @Test
  def test() = {
    println(StringEscapeUtils.escapeJava("𝕎"))
    println(StringEscapeUtils.unescapeJava("\\uD835\\uDD4E"))
  }
}