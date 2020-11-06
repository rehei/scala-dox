package com.github.rehei.scala.dox.reference

abstract class DoxBibKeyLookupBase {
  
  def lookupKey(): String
  
  def resolve(): String
  
}