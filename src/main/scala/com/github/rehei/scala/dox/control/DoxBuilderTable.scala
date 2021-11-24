package com.github.rehei.scala.dox.control

import scala.xml.NodeSeq

import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.model.DoxLikeString
import com.github.rehei.scala.dox.model.DoxReferenceFigure
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.file.DoxFile
import com.github.rehei.scala.dox.model.DoxLabelTable
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.DoxLabelTable

object DoxBuilderTable {

  def label(_labelOption: Option[DoxFile]) = new {
    def table[T <: AnyRef](_table: DoxTable[T]) = new {
      def transposed(_transposed: Boolean) = {
        val tableData = {
          if (_transposed) {
            _table.withoutColumnSpace
          } else {
            _table
          }
        }
        DoxLabelTable(_labelOption, tableData, _transposed)
      }
    }
  }

}