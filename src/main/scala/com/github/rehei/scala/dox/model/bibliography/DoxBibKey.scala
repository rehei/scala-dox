package com.github.rehei.scala.dox.model.bibliography

trait DoxBibKey {

  def name(): String

  def lookup(): DoxBibKeyLookupBase

}