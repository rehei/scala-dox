package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableMulti
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

case class DoxLabelTableMulti(label: Option[DoxReferencePersistentTable], models: DoxTableMulti, transposed: Boolean)
