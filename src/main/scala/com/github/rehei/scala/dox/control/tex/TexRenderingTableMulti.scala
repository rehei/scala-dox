package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTable

class TexRenderingTableMulti(baseAST: TexAST, floating: Boolean, models: Seq[DoxTable[_]], reference: String) {

  case class MappedTableHeadKey(content: TexCommandInline, ruleOption: Option[TexCommandInline])

  def createTableString() = {
    models.map(model => {
      new TexRenderingTable(baseAST, floating, model, reference).createTableString() ++ "\n \\vspace*{1cm}"
    }).mkString
  }

}