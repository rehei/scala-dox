package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.file.DoxReferencePersistentTable
import com.github.rehei.scala.dox.model.table.DoxTable

case class DoxLabelTableMulti(label: Option[DoxReferencePersistentTable], multiTable: Seq[DoxTable[_]], transposed: Boolean)