package com.github.rehei.scala.dox.model.bibliography

import scalaj.http.Http
import scalaj.http.HttpOptions
import org.jbibtex.BibTeXDatabase
import org.jbibtex.BibTeXEntry
import scala.collection.JavaConversions._
import org.jbibtex.Key
import com.github.rehei.scala.dox.model.ex.DoxBibKeyIntegrityException
import com.github.rehei.scala.dox.model.DoxDOI

class DoxBibKeyLookupDoi(bibKeyName: String, doi: String, year: Long, by: String, title: String) extends DoxBibKeyLookupBase {

  protected case class SingleEntry(database: BibTeXDatabase) {

    assert(database.getEntries.values().toSeq.size == 1)
    val entry = database.getEntries.values().toSeq.head

    def expect(key: Key, expected: String) = {
      val actual = entry.getField(key).toUserString()
      if (actual != expected) {
        throw new DoxBibKeyIntegrityException(getExceptionMessage(key, expected, actual))
      }
    }

    protected def getExceptionMessage(key: Key, expected: String, actual: String) = {
      s"Checking integrity for ${bibKeyName} on ${key.getValue} failed, as ${expected} was expected, but actually ${actual} was given."
    }

  }

  def resolve() = {
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
    val entry = SingleEntry(database)

    entry.expect(BibTeXEntry.KEY_DOI, doi.stripPrefix("https://doi.org/"))
    entry.expect(BibTeXEntry.KEY_YEAR, year.toString())
    entry.expect(BibTeXEntry.KEY_TITLE, title)

    DoxBibKeyLookupResult(bibKeyName, database)
  }

}