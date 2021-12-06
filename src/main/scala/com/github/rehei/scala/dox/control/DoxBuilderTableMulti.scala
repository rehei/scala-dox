package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxLabelTableMulti
import com.github.rehei.scala.dox.model.table.DoxTable

import com.github.rehei.scala.dox.model.table.DoxTableMulti
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

object DoxBuilderTableMulti {

  def label(_labelOption: Option[DoxReferencePersistentTable]) = new {
    def tables(_tables: Seq[DoxTable[_]]) = new {
      def transposed(_transposed: Boolean) = {
        DoxLabelTableMulti(_labelOption, DoxTableMulti(_tables), _transposed)
      }
    }
  }
}


