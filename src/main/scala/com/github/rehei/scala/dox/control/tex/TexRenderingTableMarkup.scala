package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.text.TextAST

class TexRenderingTableMarkup(protected val model: DoxTableMatrix, markup: TexMarkupFactory) {

  import markup._

  protected case class InnerTableOn() extends TableMode {
    def toprule() {
      renderMidRule()
    }
    def bottomrule() {
      renderMidRule()
    }
  }
  
  protected case class InnerTableOff() extends TableMode {
    def toprule() {
      \ toprule
    }
    def bottomrule() {
      \ bottomrule
    }
  }

  trait TableMode {
    def toprule(): Unit
    def bottomrule(): Unit
  }

  def getTableMode(isInnerTable: Boolean) = {
    if (isInnerTable) {
      InnerTableOn()
    } else {
      InnerTableOff()
    }
  }

  def renderMidRule() = {
    \ cmidrule { s"1-${model.dimension().size}" }
  }

  def renderValue(values: Seq[TextAST]) = {
    \ plain { values.map(Text2TEX(false).generate(_)).mkString(" & ") + "\\\\" + "\n" }
  }

  def renderSpace() = {
    \ rule & { "0pt" } { "3ex" }
  }

}