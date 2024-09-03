package com.github.rehei.scala.dox.model

import scala.xml.NodeSeq
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentImage
import scala.xml.Node

case class DoxViewModelSvg(image: Node, label: Option[DoxReferencePersistentImage])