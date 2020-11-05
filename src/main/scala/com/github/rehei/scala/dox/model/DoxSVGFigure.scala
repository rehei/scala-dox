package com.github.rehei.scala.dox.model

case class DoxSVGFigure(config: DoxFigure, val image: DoxLikeSVG) {
  
  def toSet() = {
    DoxSVGFigureSet(config, Seq(image))
  }
  
}