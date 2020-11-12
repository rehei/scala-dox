package com.github.rehei.scala.dox.model.bibliography

import org.jbibtex.BibTeXDatabase

case class DoxBibKeyLookupResult(protected val keyName: String, protected val database: BibTeXDatabase) {

  def normalize() = {
    val database = DoxBibtexParse().parse(get())
    DoxBibtexFormat("___").format(database)
  }

  def get() = {
    DoxBibtexFormat(keyName).format(database)
  }

}