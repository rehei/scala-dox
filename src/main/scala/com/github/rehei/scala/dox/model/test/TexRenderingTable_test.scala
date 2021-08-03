package com.github.rehei.scala.dox.model.test



import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.table.DoxTableKeyConfig
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.tree.DoxTreeItem

class TexRenderingTable_test(baseAST: TexAST, floating: Boolean, model: DoxTableNew[_], reference: DoxReferenceTable) {

  protected val markup = new TexMarkupFactory(baseAST)
  protected val treeLeaves = model.treeTable.leafChildrenSeq()
  import markup._

  def create() {
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      $ { _ tabularx & { \\textwidth } { getColumnConfig() } } {
        \ toprule;
        appendTableHead()
        \ midrule;
        appendTableBody()
        \ bottomrule;
      }
      \ caption & { markup.escape(model.caption) }
      \ label { reference.referenceID }
    }
    if (!floating) {
      \ FloatBarrier;
    }
  }

  protected def getColumnConfig() = {
    treeLeaves.map(node => getTexAlignment(node.config)).mkString
  }

  protected def appendTableHead() {
    for (row <- model.head) {
      \ plain { row.map(entry => columnHeader(entry)).mkString(" & ") + "\\\\" + "\n" }
    }
  }

  protected def columnHeader(entry: DoxTreeItem) = {
    if (entry.endPoints().length <= 1) {
      Text2TEX.generate(entry.config.text)
    } else {
      "\\multicolumn{" + entry.endPoints().length + "}{" + getTexAlignment(entry.config) + "}{" + Text2TEX.generate(entry.config.text) + "}"
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