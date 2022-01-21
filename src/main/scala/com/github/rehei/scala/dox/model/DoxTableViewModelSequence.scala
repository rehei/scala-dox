package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

case class DoxTableViewModelSequence(
  label:  Option[DoxReferencePersistentTable],
  models: DoxTableSequence,
  title:  TextAST)
