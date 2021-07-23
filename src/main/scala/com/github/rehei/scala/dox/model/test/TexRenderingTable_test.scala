package com.github.rehei.scala.dox.model.test

import scala.collection.mutable.ArrayBuffer
import com.github.rehei.scala.dox.model.table.DoxTableAlignment
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory
import com.github.rehei.scala.dox.control.tex.TexTableCategory

class TexRenderingTable_test(baseAST: TexAST, floating: Boolean, model: DoxTable_test, reference: DoxReferenceTable) {

  protected val markup = new TexMarkupFactory(baseAST)
  import markup._

  def create() {
    if (!floating) {
      \ FloatBarrier;
    }
    $ { _ table & { ###("H") } } {
      \ centering;
      $ { _ tabularx & { \\textwidth } { getTableConfig() } } {
        \ toprule;
        appendTableHeadKey()
        appendTableHead()
        \ midrule;
        appendTableBody()
        \ bottomrule;
      }
      \ caption & { markup.escape(model.caption()) }
      \ label { reference.referenceID }
    }
    if (!floating) {
      \ FloatBarrier;
    }

  }

  protected def getTableConfig() = {
    model.head.map(config => getTexAlignment(config)).mkString
  }

  protected def getTexAlignment(config: DoxTableKeyConfig_test) = {
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

  protected def computeCategoryLookahead(index: Int) = {

    var count = 1
    while (index + count < model.head.size && model.head(index).categoryOption.isDefined && model.head(index).categoryOption == model.head(index + count).categoryOption) {
      count = count + 1
    }
    count
  }

  protected def appendTableHeadKey() {

    val categories = model.head.flatMap(_.categoryOption)

    if (categories.size > 0) {

      var index = 0
      val bufferContent = ArrayBuffer[TexTableCategory]()

      while (index < model.head.size) {
        val lookahead = computeCategoryLookahead(index)
        val name = model.head(index).categoryOption.map(_.name).getOrElse("")

        val mappedCategory = {

          if (lookahead > 1) {
            TexTableCategory(\\ multicolumn & { lookahead } { "c" } { markup.escape(name) }, Some(\\ cmidrule & { s"${index + 1}-${index + lookahead}" }))
          } else {
            TexTableCategory(\\ multicolumn & { lookahead } { "c" } { markup.escape(name) }, None)
          }
        }

        bufferContent.append(mappedCategory)

        index = index + lookahead
      }

      \ plain { bufferContent.map(_.content.generate()).mkString(" & ") + "\\\\" }
      \ plain { bufferContent.flatMap(_.rule).map(_.generate()).mkString(" ") }
    }

  }

  protected def appendTableHead() {
    \ plain { model.head.map(config => escape(config.name)).mkString(" & ") + "\\\\" }
  }

  protected def appendTableBody() {
    for (row <- model.data) yield {
      \ plain { row.map(markup.escape(_)).mkString(" & ") + "\\\\" }
    }
  }

}