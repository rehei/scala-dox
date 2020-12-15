package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.model.DoxReferenceEquation
import com.github.rehei.scala.dox.model.DoxReferenceTable
import com.github.rehei.scala.dox.model.DoxReferenceLike
import com.github.rehei.scala.dox.model.DoxReferenceGeneric
import com.github.rehei.scala.dox.model.DoxReferenceFigure

case class DoxReferenceFactory(protected val prefix: String) {
  
  protected val nextID = NextID(prefix)
  
  def equation() = {
    DoxReferenceEquation("equation-" + nextID.nextID())
  }
  
  def figure() = {
    DoxReferenceFigure("figure-" + nextID.nextID())
  }
  
  def table() = {
    DoxReferenceTable("table-" + nextID.nextID())
  }
  
  def generic() = {
    DoxReferenceGeneric("generic-" + nextID.nextID())
  }
  
  def filename() = {
    DoxReferenceTable("file-" + nextID.nextID())
  }
  
}