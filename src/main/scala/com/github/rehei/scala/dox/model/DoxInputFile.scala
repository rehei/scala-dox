package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.util.NextID

object DoxInputFile {
  
  val next = NextID("NextFile")
  
}

case class DoxInputFile(val content: String, label: Option[DoxReferenceBase]) {

  def fileCaption = {
    label.map(m => m.name + " | " + m.hashID).getOrElse("dummylabel" + DoxInputFile.next.nextID())
  }

  def fileLabel = {
    label.map(_.hashID).getOrElse("dummylabel" + DoxInputFile.next.nextID())
  }

}