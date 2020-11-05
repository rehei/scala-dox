package com.github.rehei.scala.dox.reference

import scalaj.http.Http
import java.io.StringWriter
import scalaj.http.HttpOptions

class ReferenceLookupDoi(name: String, doi: String) extends ReferenceLookupBase {

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
    val database = BibtexParse().parse(content.body)

    BibtexFormat(name).format(database)
  }

}