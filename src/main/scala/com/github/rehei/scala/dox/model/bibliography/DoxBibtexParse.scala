package com.github.rehei.scala.dox.model.bibliography

import java.io.StringReader
import scala.collection.JavaConversions._

case class DoxBibtexParse() {
  def parse(content: String) = {
    val parser = new org.jbibtex.BibTeXParser()
    parser.parse(new StringReader(content))
  }
}
