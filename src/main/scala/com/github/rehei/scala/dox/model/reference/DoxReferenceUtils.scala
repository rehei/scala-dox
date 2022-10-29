package com.github.rehei.scala.dox.model.reference

import com.github.rehei.scala.dox.util.NextID

case class DoxReferenceUtils(prefix: String) {
  
  case class DoxReferenceWrappy(name: String, caption: String)
  
  protected val nextID = NextID("__" + prefix)
  
  def transform(input: Option[DoxReferenceBase]) = {
    input.map(_.name).getOrElse(nextID.nextID())
  }
  
}