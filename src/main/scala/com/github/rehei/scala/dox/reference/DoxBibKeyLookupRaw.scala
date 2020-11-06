package com.github.rehei.scala.dox.reference

class DoxBibKeyLookupRaw(name: String, content: String) extends DoxBibKeyLookupBase {

  def lookupKey() = {
    wrap("___") // make sure that potentially equal raw entries have the same key '___'
  }

  def resolve() = {
    wrap(name)
  }

  protected def wrap(key: String) = {
    val database = DoxBibtexParse().parse(content)
    DoxBibtexFormat(key).format(database)
  }

}