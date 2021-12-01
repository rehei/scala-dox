package com.github.rehei.scala.dox.model.file

import com.github.rehei.scala.dox.model.DoxReferenceBase

protected[dox] case class DoxFile(name: String) extends DoxReferenceBase {
  
  override def referenceID = name
  
}