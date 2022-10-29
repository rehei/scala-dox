package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

case class DoxInputFile(val content: String, label: Option[DoxReferenceBase]) {

  def fileCaption = {
    label.map(m => m.name + " | " + m.hashID).getOrElse("dummylabel")
  }

  def fileLabel = {
    label.map(_.hashID).getOrElse("dummylabel")
  }

}