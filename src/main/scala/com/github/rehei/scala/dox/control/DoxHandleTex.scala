package com.github.rehei.scala.dox.control

import java.nio.file.Path

import org.apache.commons.io.FilenameUtils

import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.util.InkscapeUtils
import com.github.rehei.scala.dox.util.SerializeSvg
import com.github.rehei.scala.dox.util.SvgMode
import com.github.rehei.scala.dox.model.DoxFileTex
import com.github.rehei.scala.dox.util.SerializeTex
import com.github.rehei.scala.dox.control.tex.TexAST
import com.github.rehei.scala.dox.control.tex.TexMarkupFactory

case class DoxHandleTex(_targetTex: Path) {

  case class TexRenderingSVG(svg: DoxSvgFigure, svgHandle: DoxHandleSvg) {
    case class SvgData(content: String, filepath: Path)

    case class HasTitle() extends SvgMode {
      import tmpMarkup._

      protected val factor = 0.9
      protected val tableTotalSize = "\\dimexpr(\\tabcolsep*2)+" + factor + "\\textwidth"
      protected val columnTotalSize = " >{\\raggedright\\arraybackslash}p{" + factor + "\\textwidth}"

      protected def appendSvgToAST(filePath: String) = {
        \ vspace { "4pt" };
        \ centering;
        appendTitle()
        \ includegraphics { filePath }
        appendBottom()
      }

      protected def appendTitle() {
        $ { _ tabular$ & { (tableTotalSize) } { columnTotalSize } } {
          \ toprule;
          \ plain { svg.titleOption.getOrElse(throw new IllegalArgumentException("Title missing")) + "\\\\" }
          \ midrule;
        }
      }

      protected def appendBottom() {
        $ { _ tabular$ & { (tableTotalSize) } { columnTotalSize } } {
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

  def serialize(svg: DoxSvgFigure, svgHandle: DoxHandleSvg) = {
    val targetTexSVG = svgHandle._targetTexSVG.normalize()
    assume(targetTexSVG.toString().startsWith(targetTex.toString()))

    val texFileGen = new SerializeTex(svgHandle._targetTexSVG)
    val svgData = TexRenderingSVG(svg, svgHandle).generate()
    val nameTex = texFileGen.generate(svgData.content, svgData.filepath).getFileName.toString()

    targetTex.relativize(targetTexSVG.resolve(nameTex)).toString()
  }

}