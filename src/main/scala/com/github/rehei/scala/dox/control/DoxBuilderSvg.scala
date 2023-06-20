package com.github.rehei.scala.dox.control

import scala.xml.NodeSeq

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentImage

object DoxBuilderSvg {

  def label(_labelOption: Option[DoxReferencePersistentImage]) = new {
    def data(_data: NodeSeq) = {
        DoxViewModelSvg(_data, _labelOption)
    }
  }

}