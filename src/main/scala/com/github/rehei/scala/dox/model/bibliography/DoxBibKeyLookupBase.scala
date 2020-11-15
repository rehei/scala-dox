package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.DoxDOI
import org.jbibtex.BibTeXDatabase

abstract class DoxBibKeyLookupBase {
  
  def resolveValidated(): DoxBibKeyLookupResult
 
  def validate(result: DoxBibKeyLookupResult)
}