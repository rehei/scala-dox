package com.github.rehei.scala.dox.control

import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxLikeSvg

abstract class DoxHandleSvg {

  def serialize(image: DoxLikeSvg): String
  
}