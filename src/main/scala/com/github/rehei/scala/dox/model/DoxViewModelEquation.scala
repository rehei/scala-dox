package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

case class DoxViewModelEquation(equation: DoxEquation, label: Option[DoxReferencePersistentEquation]) {
  def fileCaption = {
    label.map(m => m.name + " | " + m.hashID).getOrElse("dummylabel")
  }

  def fileLabel = {
    label.map(_.hashID).getOrElse("dummylabel")
  }
}