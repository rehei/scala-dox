package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.control.DoxCacheBibliography
import com.github.rehei.scala.dox.model.DoxDOI

abstract class DoxBibKeyLookupBase {
  
  def resolve(): DoxBibKeyLookupResult
  
}