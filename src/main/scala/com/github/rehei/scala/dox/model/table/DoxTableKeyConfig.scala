package com.github.rehei.scala.dox.model.table

case class DoxTableKeyConfig(
  name:           String,
  alignment:      DoxTableAlignment,
  dynamic:        Boolean,
  rendering:      DoxTableStringConversion,
  categoryOption: Option[DoxTableKeyCategory])

