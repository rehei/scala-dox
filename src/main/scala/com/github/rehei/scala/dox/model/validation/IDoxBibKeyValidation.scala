package com.github.rehei.scala.dox.model.validation

import com.github.rehei.scala.dox.model.bibliography.DoxBibKey

trait IDoxBibKeyValidation {
  
  def validate(in: DoxBibKey): Unit
  
}