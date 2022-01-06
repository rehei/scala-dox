package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation

case class DoxEquation(equation: String, label: Option[DoxReferencePersistentEquation]) {
  def fileContent() = {
    label
      .map(reference => equation + "\\label{eq:" + reference + "}")
      .getOrElse(equation)
  }
}