package com.github.rehei.scala.dox.reference

trait DoxBibKey {

  def name(): String

  def lookup(): DoxBibKeyLookupBase
  
}