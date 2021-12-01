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
import com.github.rehei.scala.dox.model.DoxLabelTableMulti

object DoxBuilderTableMulti {

  def label(_labelOption: Option[DoxFile]) = new {
    def tables(_tables: Seq[DoxTable[_]]) = new {
      def transposed(_transposed: Boolean) = {
        DoxLabelTableMulti(_labelOption, _tables, _transposed)
      }
    }
  }
}