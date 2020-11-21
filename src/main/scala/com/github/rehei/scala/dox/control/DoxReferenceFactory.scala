package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxReference
import com.github.rehei.scala.dox.util.NextID

case class DoxReferenceFactory(protected val prefix: String) {
  
  protected val nextID = NextID(prefix)
  
  def next() = {
    DoxReference(nextID.nextID())
  }
  
}