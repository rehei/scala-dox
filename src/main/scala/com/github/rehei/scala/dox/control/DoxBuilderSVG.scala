package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxLikeSVG
import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.model.DoxSVGFigureSet
import com.github.rehei.scala.dox.model.DoxLikeString
import com.github.rehei.scala.dox.model.DoxReference

object DoxBuilderSVG {

  def caption(_builder: DoxLikeString) = {
    create(_builder.get())
  }

  def caption(_caption: String) = {
    create(_caption)
  }

  protected def create(_caption: String) = new {
    def label(_labelOption: Option[DoxReference]) = new {
      def data(_data: DoxLikeSVG) = {
        create(Seq(_data))
      }
      def data(_data: Seq[DoxLikeSVG]) = {
        create(_data)
      }
      protected def create(_data: Seq[DoxLikeSVG]) = {
        DoxSVGFigureSet(DoxFigure(_caption, _labelOption), _data)
      }
    }
  }

}