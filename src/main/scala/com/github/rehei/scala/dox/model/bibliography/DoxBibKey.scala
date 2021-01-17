package com.github.rehei.scala.dox.model.bibliography

import com.github.rehei.scala.dox.model.DoxValueDOI
import org.apache.commons.lang3.StringUtils
import com.github.rehei.scala.dox.model.ex.DoxBibKeyValueBlankException
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNameAsciiException

trait DoxBibKey {

  def name: String

  def documentID: Option[DoxValueDOI]

  def lookup: DoxBibKeyLookupBase

}