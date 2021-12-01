package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxLabelTable
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

object DoxBuilderTable {

  def label(_labelOption: Option[DoxReferencePersistentTable]) = new {
    def table[T <: AnyRef](_table: DoxTable[T]) = new {
      def transposed(_transposed: Boolean) = {
        DoxLabelTable(_labelOption, _table, _transposed)
      }
    }
  }

}