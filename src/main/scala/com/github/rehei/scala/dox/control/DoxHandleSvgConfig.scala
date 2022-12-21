package com.github.rehei.scala.dox.control

case class DoxHandleSvgConfig(val scale: Double) {

  assume(scale <= 1)
  assume(scale >= 0)
  
  def adjust(value: Double) = {
    scale * value
  }

}