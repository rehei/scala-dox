package com.github.rehei.scala.dox.model.bibliography

import scalaj.http.Http
import scalaj.http.HttpOptions
import org.jbibtex.BibTeXDatabase
import org.jbibtex.BibTeXEntry
import scala.collection.JavaConversions._
import org.jbibtex.Key
import com.github.rehei.scala.dox.model.ex.DoxBibKeyIntegrityException
import com.github.rehei.scala.dox.model.DoxValueDOI
import com.github.rehei.scala.dox.util.NormalizeUtils

class DoxBibKeyLookupDOI(bibKeyName: String, doi: DoxValueDOI,
                         yearOption:  Option[Long],
                         byOption:    Option[String],
                         titleOption: Option[String]) extends DoxBibKeyLookupBase {

  def resolveValidated() = {

    val handle = DoxBibKeyLookupDOIHandle(doi)
    val database = DoxBibtexParse().parse(handle.content())

    val result = DoxBibKeyLookupResult(bibKeyName, database)

    validate(result)

    result
  }

  def validate(result: DoxBibKeyLookupResult) {
    val entry = DoxBibtexParseSingleEntry(bibKeyName, result.database)

    entry.expectNormalized(BibTeXEntry.KEY_DOI, doi.value.stripPrefix("https://doi.org/").stripPrefix("https://dx.doi.org/"))

    for (by <- byOption) {
      entry.expectAnyWordNormalized(BibTeXEntry.KEY_AUTHOR, by.toString())
    }
    for (year <- yearOption) {
      entry.expectNormalized(BibTeXEntry.KEY_YEAR, year.toString())
    }
    for (title <- titleOption) {
      entry.expectNormalized(BibTeXEntry.KEY_TITLE, title)
    }
  }



}