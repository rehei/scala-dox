package com.github.rehei.scala.dox.control

import scala.xml.NodeSeq

import com.github.rehei.scala.dox.model.DoxFigure
import com.github.rehei.scala.dox.model.DoxLikeString
import com.github.rehei.scala.dox.model.DoxSvgFigure
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentImage

object DoxBuilderSvg {

  def label(_labelOption: Option[DoxReferencePersistentImage]) = new {
    def data(_data: NodeSeq) = {
      DoxSvgFigure(DoxFigure(_labelOption), _data)
    }
  }

}