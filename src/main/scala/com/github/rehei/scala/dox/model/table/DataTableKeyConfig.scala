package com.github.rehei.scala.dox.model.table

case class DataTableKeyConfig(
  name:           String,
  alignment:      DataTableAlignment,
  dynamic:        Boolean,
  rendering:      DataTableStringConversion,
  categoryOption: Option[DataTableKeyCategory])

