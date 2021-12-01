package com.github.rehei.scala.dox.control

import scala.xml.NodeSeq

import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.model.DoxLikeString
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.file.DoxPersistentTable
import com.github.rehei.scala.dox.model.file.DoxPersistentImage

object DoxBuilderSvg {

  def caption(_builder: DoxLikeString) = {
    create(_builder.get())
  }

  def caption(_caption: String) = {
    create(_caption)
  }

  protected def create(_caption: String) = new {
    def label(_labelOption: Option[DoxPersistentImage]) = new {
      def data(_data: NodeSeq) = {
        DoxSvgFigure(DoxFigure(_caption, _labelOption), _data)
      }
    }
  }

}