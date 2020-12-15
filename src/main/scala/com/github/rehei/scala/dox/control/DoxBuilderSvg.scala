package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.model.DoxLikeSvg
import com.github.rehei.scala.dox.model.DoxLikeString
import com.github.rehei.scala.dox.model.DoxReferenceFigure
import com.github.rehei.scala.dox.model.DoxSvgFigureSet

object DoxBuilderSvg {

  def caption(_builder: DoxLikeString) = {
    create(_builder.get())
  }

  def caption(_caption: String) = {
    create(_caption)
  }

  protected def create(_caption: String) = new {
    def label(_labelOption: Option[DoxReferenceFigure]) = new {
      def data(_data: DoxLikeSvg) = {
        create(Seq(_data))
      }
      def data(_data: Seq[DoxLikeSvg]) = {
        create(_data)
      }
      protected def create(_data: Seq[DoxLikeSvg]) = {
        DoxSvgFigureSet(DoxFigure(_caption, _labelOption), _data)
      }
    }
  }

}