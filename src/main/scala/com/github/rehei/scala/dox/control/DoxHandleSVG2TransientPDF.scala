package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxLikeSVG
import java.nio.file.Path
import com.github.rehei.scala.dox.util.SVG2File
import org.apache.commons.io.FilenameUtils
import com.github.rehei.scala.dox.util.InkscapeUtils

class DoxHandleSVG2TransientPDF(_targetTex: Path, _targetTexSVG: Path) extends DoxHandleSVG {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()

  protected val inkscape = new InkscapeUtils(targetTexSVG)

  assume(targetTexSVG.toString().startsWith(targetTex.toString()), "targetTexSVG should be within targetTex")

  protected val svgFileGen = new SVG2File(targetTexSVG)

  def serialize(image: DoxLikeSVG): String = {
    val nameSVG = svgFileGen.generateSVGFile(image).getFileName.toString()
    val name = FilenameUtils.removeExtension(nameSVG)

    targetTex.relativize(targetTexSVG.resolve(name + ".pdf")).toString()
  }
  
  def generatePDFs() = {
    inkscape.generatePDFs()
  }

}