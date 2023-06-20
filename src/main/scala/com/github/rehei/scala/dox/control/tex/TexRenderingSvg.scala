package com.github.rehei.scala.dox.control.tex

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.control.DoxHandleSvg
import java.nio.file.Path
import com.github.rehei.scala.dox.control.DoxHandleSvgConfig

class TexRenderingSVG(config: DoxHandleSvgConfig, include: String) {

  def generate() = {
    val texWrapping = {
      TexRenderingSvgStandalone(include)
    }

    texWrapping.generate().build()
  }
}