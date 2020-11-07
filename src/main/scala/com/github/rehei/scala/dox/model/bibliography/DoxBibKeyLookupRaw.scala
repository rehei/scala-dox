package com.github.rehei.scala.dox.model.bibliography

class DoxBibKeyLookupRaw(bibKeyName: String, content: String) extends DoxBibKeyLookupBase {

  def lookupKey() = {
    wrap("___") // make sure that potentially equal raw entries have the same key '___'
  }

  def resolve() = {
    wrap(bibKeyName)
  }

  protected def wrap(key: String) = {
    val database = DoxBibtexParse().parse(content)
    DoxBibtexFormat(key).format(database)
  }

}