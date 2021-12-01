package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

case class DoxLabelTable[T <: AnyRef](label: Option[DoxReferencePersistentTable], model: DoxTable[T], transposed: Boolean)