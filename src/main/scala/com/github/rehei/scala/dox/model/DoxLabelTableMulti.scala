package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.file.DoxFile
import com.github.rehei.scala.dox.model.table.DoxTable

case class DoxLabelTableMulti(label: Option[DoxFile], multiTable: Seq[DoxTable[_]], transposed: Boolean)