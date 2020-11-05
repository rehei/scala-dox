package com.github.rehei.scala.dox.reference

abstract class ReferenceLookupBase {
  
  def lookupKey(): String
  
  def resolve(): String
  
}