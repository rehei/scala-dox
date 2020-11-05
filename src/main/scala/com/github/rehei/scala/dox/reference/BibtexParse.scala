package com.github.rehei.scala.dox.reference

import java.io.StringReader

case class BibtexParse() {
  def parse(content: String) = {
    val parser = new org.jbibtex.BibTeXParser()
    parser.parse(new StringReader(content))
  }
}
