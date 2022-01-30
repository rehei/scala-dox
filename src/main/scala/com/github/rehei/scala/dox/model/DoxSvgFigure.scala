package com.github.rehei.scala.dox.model

import scala.xml.NodeSeq
import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentImage

case class DoxSvgFigure(image: NodeSeq, label: Option[DoxReferencePersistentImage])