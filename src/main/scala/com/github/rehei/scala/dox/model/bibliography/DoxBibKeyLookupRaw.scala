package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.control.DoxCacheBibliography

class DoxBibKeyLookupRaw(bibKeyName: String, content: String) extends DoxBibKeyLookupBase {

  def resolve() = {
    val database = DoxBibtexParse().parse(content)
    
    DoxBibKeyLookupResult(bibKeyName, database)
  }

}