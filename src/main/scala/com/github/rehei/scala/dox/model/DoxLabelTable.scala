package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable

case class DoxLabelTable[T <: AnyRef](label: String, model: DoxTable[T])