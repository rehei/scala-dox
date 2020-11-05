package com.github.rehei.scala.dox.reference

trait ReferenceKey {

  def name(): String

  def lookup(): ReferenceLookupBase
  
}