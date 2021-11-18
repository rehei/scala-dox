package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.file.DoxFile

case class DoxLabelTable[T <: AnyRef](label: Option[DoxFile], model: DoxTable[T])