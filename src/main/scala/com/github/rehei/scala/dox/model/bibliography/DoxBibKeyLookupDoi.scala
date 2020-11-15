package com.github.rehei.scala.dox.model.bibliography

import scalaj.http.Http
import scalaj.http.HttpOptions
import org.jbibtex.BibTeXDatabase
import org.jbibtex.BibTeXEntry
import scala.collection.JavaConversions._
import org.jbibtex.Key
import com.github.rehei.scala.dox.model.ex.DoxBibKeyIntegrityException
import com.github.rehei.scala.dox.model.DoxDOI
import com.github.rehei.scala.dox.util.NormalizeUtils

class DoxBibKeyLookupDoi(bibKeyName: String, doi: String, year: Long, by: String, title: String) extends DoxBibKeyLookupBase {

  def resolveValidated() = {
    val content = {
      Http(this.doi)
        .header("Accept", "application/x-bibtex")
        .option(HttpOptions.followRedirects(true))
        .timeout(20 * 1000, 100 * 1000)
        .asString
    }

    if (!content.isSuccess) {
      throw new RuntimeException("Could not resolve DOI" + content.body)
    }
    val database = DoxBibtexParse().parse(content.body)

    val result = DoxBibKeyLookupResult(bibKeyName, database)

    validate(result)

    result
  }

  def validate(result: DoxBibKeyLookupResult) {
    val entry = DoxBibtexParseSingleEntry(bibKeyName, result.database)

    entry.expectNormalized(BibTeXEntry.KEY_DOI, doi.stripPrefix("https://doi.org/").stripPrefix("http://dx.doi.org/"))
    entry.expectAnyWordNormalized(BibTeXEntry.KEY_AUTHOR, by.toString())
    entry.expectNormalized(BibTeXEntry.KEY_YEAR, year.toString())
    entry.expectNormalized(BibTeXEntry.KEY_TITLE, title)
  }

}