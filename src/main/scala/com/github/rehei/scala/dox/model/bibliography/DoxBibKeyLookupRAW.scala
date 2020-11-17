package com.github.rehei.scala.dox.model.bibliography

import org.jbibtex.BibTeXDatabase
import com.github.rehei.scala.dox.model.DoxValueRAW

class DoxBibKeyLookupRAW(bibKeyName: String, content: DoxValueRAW) extends DoxBibKeyLookupBase {

  def resolveValidated() = {
    val database = DoxBibtexParse().parse(content.value)

    DoxBibKeyLookupResult(bibKeyName, database)
  }

  def validate(result: DoxBibKeyLookupResult) {
    val actual = result.normalize()
    val expected = resolveValidated().normalize()

    assert(trimString(actual) == trimString(expected))
  }

  protected def trimString(input: String) = {
    input.replace("\n", "").replace("\t", "").replace(" ", "")
  }
}