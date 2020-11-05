package com.github.rehei.scala.dox.reference

class ReferenceLookupRaw(name: String, content: String) extends ReferenceLookupBase {

  def lookupKey() = {
    wrap("___") // make sure that potentially equal raw entries have the same key '___'
  }

  def resolve() = {
    wrap(name)
  }

  protected def wrap(key: String) = {
    val database = BibtexParse().parse(content)
    BibtexFormat(key).format(database)
  }

}