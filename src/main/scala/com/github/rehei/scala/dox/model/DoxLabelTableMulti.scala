package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

case class DoxLabelTableMulti(label: Option[DoxReferencePersistentTable], multiTable: Seq[DoxTable[_]], transposed: Boolean)