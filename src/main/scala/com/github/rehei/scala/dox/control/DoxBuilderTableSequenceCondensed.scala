package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.model.DoxViewModelTableSequence
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable
import com.github.rehei.scala.dox.model.table.content.DoxContent.DoxLegend

object DoxBuilderTableSequenceCondensed {

  def label(_labelOption: Option[DoxReferencePersistentTable]) = new {
    def data(_tables: Seq[DoxTable[_]]) = {
      DoxViewModelTableSequence(_labelOption, DoxTableSequence(_tables.map(_.transform())), None, None)
    }
  }

}