package com.github.rehei.scala.dox.model

case class DoxSvgFigure(config: DoxFigure, val image: DoxLikeSvg) {
  
  def toSet() = {
    DoxSvgFigureSet(config, Seq(image))
  }
  
}