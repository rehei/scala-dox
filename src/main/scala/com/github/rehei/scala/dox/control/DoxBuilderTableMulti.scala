package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxLabelTableMulti
import com.github.rehei.scala.dox.model.file.DoxFile
import com.github.rehei.scala.dox.model.table.DoxTable

object DoxBuilderTableMulti {

  def label(_labelOption: Option[DoxFile]) = new {
    def tables(_tables: Seq[DoxTable[_]]) = new {
      def transposed(_transposed: Boolean) = {
        DoxLabelTableMulti(_labelOption, _tables, _transposed)
      }
    }
  }
}