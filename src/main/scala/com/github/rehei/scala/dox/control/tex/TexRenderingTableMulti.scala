package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.DoxTableKeyNode
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.DoxTableHeadRowKey
import com.github.rehei.scala.dox.model.table.DoxTable
import com.github.rehei.scala.dox.model.table.DoxTableHeadRow
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfigExtended

class TexRenderingTableMulti(baseAST: TexAST, floating: Boolean, models: Seq[DoxTable[_]], reference: String) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])

  def createTableString() = {
    models.map(model => {
      new TexRenderingTable(baseAST, floating, model, reference).createTableString() ++ "\n \\vspace*{1cm}"
    }).mkString
  }

}