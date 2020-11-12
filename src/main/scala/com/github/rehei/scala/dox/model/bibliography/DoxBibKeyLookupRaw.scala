package com.github.rehei.scala.dox.model.bibliography

class DoxBibKeyLookupRaw(bibKeyName: String, content: String) extends DoxBibKeyLookupBase {

  def resolve() = {
    val database = DoxBibtexParse().parse(content)
    
    DoxBibKeyLookupResult(bibKeyName, database)
  }

}