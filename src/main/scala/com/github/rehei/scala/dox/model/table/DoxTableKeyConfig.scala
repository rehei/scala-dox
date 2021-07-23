package com.github.rehei.scala.dox.model.table

import com.github.rehei.scala.dox.text.TextAST

case class DoxTableKeyConfig(
  text:           TextAST,
  alignment:      DoxTableAlignment,
  dynamic:        Boolean,
  rendering:      DoxTableStringConversion,
  categoryOption: Option[DoxTableKeyCategory])

