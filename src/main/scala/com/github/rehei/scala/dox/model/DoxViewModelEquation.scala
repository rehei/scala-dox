package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferencePersistentEquation
import com.github.rehei.scala.dox.model.reference.DoxReferenceBase

case class DoxViewModelEquation(equation: DoxEquation, label: Option[DoxReferencePersistentEquation])