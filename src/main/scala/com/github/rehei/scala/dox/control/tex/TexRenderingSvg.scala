package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.control.DoxHandleSvg
import java.nio.file.Path

class TexRenderingSVG(svg: DoxSvgFigure, svgHandle: DoxHandleSvg) {

  protected val tmpAST = new TexAST
  protected val tmpMarkup = new TexMarkupFactory(tmpAST)
  import tmpMarkup._

  case class SvgData(content: String, filepath: Path)

  abstract class SvgMode() {

    protected def appendSvgToAST(filePath: String): Unit

    def generate() = {
      val filepath = createFileAndGetPath()
      appendSvgToAST(filepath.toString())
      SvgData(tmpAST.build(), filepath)
    }

    protected def createFileAndGetPath() = {
      svgHandle.serialize(svg)
    }
  }

  protected val svgMode = {
    if (svg.titleOption.isDefined) {
      HasTitle()
    } else {
      HasNoTitle()
    }
  }

  case class HasTitle() extends SvgMode {

    protected val factor = 0.9
    protected val tableTotalSize = factor + "\\textwidth"
    protected val columnAlignment = "l"

    protected def appendSvgToAST(filePath: String) = {
      \ vspace { "4pt" };
      \ centering;
      appendTitle()
      \ includegraphics { filePath }
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

  case class HasNoTitle() extends SvgMode {

    protected def appendSvgToAST(filePath: String) = {
      \ vspace { "4pt" };
      \ centering;
      \ includegraphics { filePath }
    }

  }

  def generate() = {
    svgMode.generate()
  }
}