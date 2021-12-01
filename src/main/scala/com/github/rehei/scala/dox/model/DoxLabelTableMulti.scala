package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.file.DoxPersistentTable
import com.github.rehei.scala.dox.model.table.DoxTable

case class DoxLabelTableMulti(label: Option[DoxPersistentTable], multiTable: Seq[DoxTable[_]], transposed: Boolean)