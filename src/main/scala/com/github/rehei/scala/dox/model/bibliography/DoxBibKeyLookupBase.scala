package com.github.rehei.scala.dox.model.bibliography

abstract class DoxBibKeyLookupBase {
  
  def lookupKey(): String
  
  def resolve(): String
  
}