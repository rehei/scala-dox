package com.github.rehei.scala.dox.control

import scala.xml.NodeSeq

import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.model.DoxLikeString
import com.github.rehei.scala.dox.model.DoxReferenceFigure
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.file.DoxFile
import com.github.rehei.scala.dox.model.DoxLabelTable
import com.github.rehei.scala.dox.model.table.DoxTable

object DoxBuilderTable {

  def label(_label: String) = new {
    def table[T <: AnyRef](_data: DoxTable[T]) = {
      DoxLabelTable(_label, _data)
    }
  }

}