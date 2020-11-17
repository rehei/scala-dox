package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.DoxValueDOI
import org.apache.commons.lang3.StringUtils
import com.github.rehei.scala.dox.model.ex.DoxBibKeyValueBlankException

trait DoxBibKey {

  def name: String

  def canonicalName: String

  def documentID: Option[DoxValueDOI]

  def lookup: DoxBibKeyLookupBase

}