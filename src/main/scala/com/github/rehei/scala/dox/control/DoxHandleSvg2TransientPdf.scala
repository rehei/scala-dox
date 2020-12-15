package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxLikeSvg
import java.nio.file.Path
import com.github.rehei.scala.dox.util.Svg2File
import org.apache.commons.io.FilenameUtils
import com.github.rehei.scala.dox.util.InkscapeUtils

class DoxHandleSvg2TransientPdf(_targetTex: Path, _targetTexSVG: Path) extends DoxHandleSvg {

  protected val targetTex = _targetTex.normalize()
  protected val targetTexSVG = _targetTexSVG.normalize()

  protected val inkscape = new InkscapeUtils(targetTexSVG)

  assume(targetTexSVG.toString().startsWith(targetTex.toString()), "targetTexSVG should be within targetTex")

  protected val svgFileGen = new Svg2File(targetTexSVG)

  def serialize(image: DoxLikeSvg): String = {
    val nameSVG = svgFileGen.generate(image).getFileName.toString()
    val name = FilenameUtils.removeExtension(nameSVG)

    targetTex.relativize(targetTexSVG.resolve(name + ".pdf")).toString()
  }
  
  def generatePDFs() = {
    inkscape.generatePDFs()
  }

}