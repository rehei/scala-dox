package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.DoxValueRAW

case class DoxBibKeyValueRAW(protected val _name: String, protected val _content: DoxValueRAW) extends DoxBibKey {
  
  def name = _name 
  
  def documentID = None
  
  def lookup = new DoxBibKeyLookupRAW(_name, _content)

}