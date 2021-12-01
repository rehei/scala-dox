package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.model.DoxReferenceEquation
import com.github.rehei.scala.dox.model.DoxReferenceBase
import com.github.rehei.scala.dox.model.DoxReferenceGeneric

case class DoxReferenceFactory(protected val prefix: String) {

  protected val nextID = NextID(prefix)

  def equation() = {
    DoxReferenceEquation("equation-" + nextID.nextID())
  }

  def generic() = {
    DoxReferenceGeneric("generic-" + nextID.nextID())
  }

}