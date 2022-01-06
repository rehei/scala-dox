package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxTableViewModel
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable
import com.github.rehei.scala.dox.model.table.DoxTable

object DoxBuilderTable {

  def label(_labelOption: Option[DoxReferencePersistentTable]) = new {
    def data[T <: AnyRef](_table: DoxTable[T]) = new {
      def transposed(_transposed: Boolean) = {
        DoxTableViewModel(_table, _transposed, _labelOption)
      }
    }
  }

}