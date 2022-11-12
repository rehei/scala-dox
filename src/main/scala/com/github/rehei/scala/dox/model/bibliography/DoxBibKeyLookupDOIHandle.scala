package com.github.rehei.scala.dox.model.bibliography

import scalaj.http.Http
import scalaj.http.HttpOptions
import com.github.rehei.scala.dox.model.DoxValueDOI

case class DoxBibKeyLookupDOIHandle(doi: DoxValueDOI) {

  def content() = {
    val body = get()
    clean(body)
  }

  protected def clean(body: String) = {
    body.replace("""{\&}amp$\mathsemicolon$""", """{\&}""")
  }

  protected def get() = {
    val content = {
      Http(doi.value)
        .header("Accept", "application/x-bibtex")
        .option(HttpOptions.followRedirects(true))
        .timeout(50 * 1000, 100 * 1000)
        .asString
    }

    if (!content.isSuccess) {
      throw new RuntimeException("Could not resolve DOI" + content.body)
    }

    content.body
  }

}