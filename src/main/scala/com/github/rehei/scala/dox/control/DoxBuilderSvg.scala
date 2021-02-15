package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.model.DoxLikeSvg
import com.github.rehei.scala.dox.model.DoxLikeString
import com.github.rehei.scala.dox.model.DoxReferenceFigure
import com.github.rehei.scala.dox.model.DoxFile
import com.github.rehei.scala.dox.model.DoxSvgFigure

object DoxBuilderSvg {

  def caption(_builder: DoxLikeString) = {
    create(_builder.get())
  }

  def caption(_caption: String) = {
    create(_caption)
  }

  protected def create(_caption: String) = new {
    def label(_labelOption: Option[DoxReferenceFigure]) = new {
      def file(_fileOption: Option[String]) = new {
        def data(_data: DoxLikeSvg) = {
          DoxSvgFigure(DoxFigure(_caption, _labelOption, _fileOption.map(DoxFile(_))), _data)
        }
      }
    }
  }

}