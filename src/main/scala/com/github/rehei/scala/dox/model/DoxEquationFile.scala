package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation

case class DoxEquationFile(fileContent: String, label: Option[DoxReferencePersistentEquation])