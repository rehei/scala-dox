package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.control.DoxHandleSvg
import java.nio.file.Path

class TexRenderingSVG(svg: DoxViewModelSvg, include: Path) {

  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  abstract class TexWrappingSvg() {

    def generate(): Unit

  }

  case class TitleWrapping() extends TexWrappingSvg {

    protected val factor = 0.9
    protected val tableTotalSize = factor + "\\textwidth"
    protected val columnAlignment = "l"

    override def generate() = {
      \ vspace { "4pt" };
      \ centering;
      appendTitle()
      \ includegraphics { include.toString() }
      appendBottom()
    }

    protected def appendTitle() {
      $ { _ tabular$ & { (tableTotalSize) } { columnAlignment } } {
        \ toprule;
        \ plain { svg.titleOption.getOrElse(throw new IllegalArgumentException("Title missing")) + "\\\\" }
        \ midrule;
      }
    }

    protected def appendBottom() {
      $ { _ tabular$ & { (tableTotalSize) } { columnAlignment } } {
        \ bottomrule;
      }
    }
  }

  case class NoTitleWrapping() extends TexWrappingSvg {

    override def generate() = {
      \ vspace { "4pt" };
      \ centering;
      \ includegraphics { include.toString() }
    }

  }

  def generate() = {

    val texWrapping = {
      if (svg.titleOption.isDefined) {
        TitleWrapping()
      } else {
        NoTitleWrapping()
      }
    }

    texWrapping.generate()

    tmpAST.build()
  }
}