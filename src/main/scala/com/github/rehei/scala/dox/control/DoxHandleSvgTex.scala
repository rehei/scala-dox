package com.github.rehei.scala.dox.control

import java.nio.file.Path

import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.SerializeTex
import com.github.rehei.scala.dox.util.SvgMode

case class DoxHandleSvgTex(_targetTex: Path, _targetTexSVG: Path) {

  case class TexRenderingSVG(svg: DoxSvgFigure, svgHandle: DoxHandleSvg) {
    case class SvgData(content: String, filepath: Path)

    case class HasTitle() extends SvgMode {
      import tmpMarkup._

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
      import tmpMarkup._

      protected def appendSvgToAST(filePath: String) = {
        \ vspace { "4pt" };
        \ centering;
        \ includegraphics { filePath }
      }

    }

    abstract class SvgMode() {
      protected val tmpAST = new TexAST
      protected val tmpMarkup = new TexMarkupFactory(tmpAST)
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

    def generate() = {
      svgMode.generate()
    }
  }

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()
  assume(targetTexSVG.toString().startsWith(targetTex.toString()))

  protected val svgHandle = DoxHandleSvg(SvgMode.PDF, _targetTex, _targetTexSVG)

  def serialize(svg: DoxSvgFigure) = {

    val texFileGen = new SerializeTex(svgHandle._targetTexSVG)
    val svgData = TexRenderingSVG(svg, svgHandle).generate()
    val nameTex = texFileGen.generate(svgData.content, svgData.filepath).getFileName.toString()

    targetTex.relativize(targetTexSVG.resolve(nameTex)).toString()
  }

  def transform() = {
    svgHandle.transform()
  }

}