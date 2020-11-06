package com.github.rehei.scala.dox.reference

import scalaj.http.Http
import java.io.StringWriter
import scalaj.http.HttpOptions

class DoxBibKeyLookupDoi(name: String, doi: String) extends DoxBibKeyLookupBase {

  def lookupKey(): String = {
    doi
  }
  
  def resolve() = {
    val content = {
      Http(this.doi)
        .header("Accept", "application/x-bibtex")
        .option(HttpOptions.followRedirects(true))
        .asString
    }

    if (!content.isSuccess) {
      throw new RuntimeException("Could not resolve DOI")
    }
    val database = DoxBibtexParse().parse(content.body)

    DoxBibtexFormat(name).format(database)
  }

}