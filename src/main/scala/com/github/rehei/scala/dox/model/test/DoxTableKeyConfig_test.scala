package com.github.rehei.scala.dox.model.test

import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyCategory
import com.github.rehei.scala.dox.model.table.DoxTableStringConversion

case class DoxTableKeyConfig_test(
  name:           String,
  alignment:      DoxTableAlignment,
  dynamic:        Boolean,
  rendering:      DoxTableStringConversion,
  multiColumn:    Option[Int],
  categoryOption: Option[DoxTableKeyCategory],
  parentOption:   Option[DoxTableKeyConfig_test])

