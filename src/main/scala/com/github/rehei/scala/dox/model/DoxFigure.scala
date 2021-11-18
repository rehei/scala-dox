package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.control.DoxRenderingBase
import com.github.rehei.scala.dox.model.file.DoxFile

case class DoxFigure(caption: String, label: Option[DoxFile])