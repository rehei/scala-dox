package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxViewModelTableSequence
import com.github.rehei.scala.dox.model.table.DoxTable

import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable
import com.github.rehei.scala.dox.model.table.content.DoxContent.DoxLegend

object DoxBuilderTableSequence {

  def label(_labelOption: Option[DoxReferencePersistentTable]) = new {
    def data(_tables: Seq[DoxTable[_]]) = new {
      def title(_title: String) = new {
        def annotation(_annotation: Option[DoxLegend]) = {
          DoxViewModelTableSequence(_labelOption, DoxTableSequence(_tables.map(_.transform())), _annotation, TextFactory.text(_title))
        }
      }
    }
  }
}


