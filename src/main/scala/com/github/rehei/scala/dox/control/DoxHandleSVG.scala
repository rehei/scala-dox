package com.github.rehei.scala.dox.control

import java.nio.file.Path
import com.github.rehei.scala.dox.model.DoxLikeSVG

abstract class DoxHandleSVG {

  def serialize(image: DoxLikeSVG): Path
  
}