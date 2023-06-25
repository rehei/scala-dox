package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.table.DoxTableMatrix
import com.github.rehei.scala.dox.text.TextAST
import com.github.rehei.scala.dox.text.util.Text2TEX
import com.github.rehei.scala.dox.model.table.content.DoxContent.DoxLegend

class TexRenderingTableLegend(prefix: String, protected val legend: Seq[DoxLegend]) {

  protected val tmpAST = TexAST()
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  def createTableString() = {
    createLegendTable()
    tmpAST.build()
  }

  protected def createLegendTable() {
    columnSeparation()
    $ { _ tabular { "@{}ll" } } {
      appendTableLegend()
    }
  }

  protected def appendTableLegend() {
    val tableLegend = {
      (for (row <- legend; (content, index) <- row.content.zipWithIndex) yield {

        val isFirst = (index == 0)

        texRow(isFirst, content)

      }).mkString
    }
    \ plain { tableLegend }
  }

  protected def columnSeparation() = {
    \ plain { "\\setlength{\\tabcolsep}{0.1em}" }
  }

  protected def texRow(isFirst: Boolean, content: TextAST) = {
    val texPrefix = if (isFirst) { prefix + ":" } else { "" }
    val texContent = Text2TEX(false).generate(content)

    Seq(texPrefix, texContent).map(markup).mkString(" & ") + lineEnd
  }

  protected def markup(content: String) = {
    "\\scriptsize \\textit {" + content + "}"
  }

  protected def lineEnd() = {
    "\\\\" + "\n"
  }
}