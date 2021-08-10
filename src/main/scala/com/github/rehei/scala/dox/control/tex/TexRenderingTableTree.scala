package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.tree.DoxNode
import com.github.rehei.scala.dox.model.table.tree.DoxTableTreeHeadRowKeyWithOffset
import com.github.rehei.scala.dox.model.table.tree.DoxTableTreeHeadRowKey
import com.github.rehei.scala.dox.model.table.tree.DoxTableTree

class TexRenderingTableTree(baseAST: TexAST, floating: Boolean, model: DoxTableTree[_], reference: DoxReferenceTable) {

  protected val markup = new TexMarkupFactory(baseAST)

  import markup._

  def create() {
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      $ { _ tabularx & { \\hsize } { getColumnConfig() } } {
        \ toprule;
        appendTableHead()
        \ midrule;
        appendTableBody()
        \ bottomrule;
      }
      \ caption & { model.caption }
      \ label { reference.referenceID }
    }
    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def getColumnConfig() = {
    model.root.leavesRecursive().map(node => getTexAlignment(node.config)).mkString
  }

  protected def appendTableHead() {
    for (row <- model.head.list()) {
      \ plain { row.values.map(columnHeader(_)).mkString(" & ") + "\\\\" + withOffset(row.values).map(cmidrule(_)).mkString(" ") + "\n" }
    }
  }

  protected def columnHeader(entry: DoxTableTreeHeadRowKey) = {
    if (!entry.isMultiColumn()) {
      Text2TEX.generate(entry.config.text)
    } else {
      "\\multicolumn{" + entry.size + "}{" + getTexAlignment(entry.config) + "}{" + Text2TEX.generate(entry.config.text) + "}"
    }
  }
  
  protected def withOffset(input: Seq[DoxTableTreeHeadRowKey]) = {
    var offset = 0
    for(Seq(first, second) <- input.sliding(2)) yield {
      val result = DoxTableTreeHeadRowKeyWithOffset(offset, first)
      offset = offset + first.size
      result
    }
  }

  protected def cmidrule(entry: DoxTableTreeHeadRowKeyWithOffset) = {
    if (entry.key.rule) {
      val offset = entry.offset
      val target = entry.offset + entry.key.size
      s"\\cmidrule{${offset}-${target}} "
    } else {
      ""
    }
  }

  protected def appendTableBody() {
    for (row <- model.data) yield {
      \ plain { row.map(markup.escape(_)).mkString(" & ") + "\\\\" + "\n" }
    }
  }

  protected def getTexAlignment(config: DoxTableKeyConfig) = {
    if (config.dynamic) {
      "X"
    } else {
      config.alignment match {
        case DoxTableAlignment.LEFT  => "l"
        case DoxTableAlignment.RIGHT => "r"
        case _                       => "c"
      }
    }
  }

}