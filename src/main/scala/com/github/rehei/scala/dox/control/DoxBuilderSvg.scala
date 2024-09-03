package com.github.rehei.scala.dox.control

import scala.xml.NodeSeq

import com.github.rehei.scala.dox.model.DoxViewModelSvg
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentImage
import scala.xml.Node

object DoxBuilderSvg {

  def label(_labelOption: Option[DoxReferencePersistentImage]) = new {
    def data(_data: Node) = {
        DoxViewModelSvg(_data, _labelOption)
    }
  }

}