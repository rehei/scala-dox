package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.file.DoxPersistentTable

case class DoxLabelTable[T <: AnyRef](label: Option[DoxPersistentTable], model: DoxTable[T], transposed: Boolean)