package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.DoxValueDOI

case class DoxBibKeyValueDOI(protected val _name: String, protected val _doi: DoxValueDOI) extends DoxBibKey {
  
  def name = _name 
  
  def canonicalName = "inline-doi-" + _name 

  def documentID = Some(_doi)
  
  def lookup = new DoxBibKeyLookupDOI(_name, _doi, None, None, None)

}