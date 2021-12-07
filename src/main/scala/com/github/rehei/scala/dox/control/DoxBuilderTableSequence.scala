package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxTableViewModelSequence
import com.github.rehei.scala.dox.model.table.DoxTable

import com.github.rehei.scala.dox.model.table.DoxTableSequence
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.TextFactory
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentTable

object DoxBuilderTableSequence {

  def label(_labelOption: Option[DoxReferencePersistentTable]) = new {
    def tables(_tables: Seq[DoxTable[_]]) = new {
      def title(_title: Option[String]) = new {
        def transposed(_transposed: Boolean) = {
          DoxTableViewModelSequence(_labelOption, DoxTableSequence(_tables), titleOption(_title), _transposed)
        }
      }
    }
  }

  protected def titleOption(text: Option[String]) = {
    text match {
      case Some(m) => Some(TextFactory.text(m))
      case None    => None
    }
  }
}


