package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.DoxDOI

trait DoxBibKey {

  def name: String
  
  def canonicalName: String

  def documentID: Option[DoxDOI]
  
  def lookup: DoxBibKeyLookupBase

  def validate(): Unit
  
}