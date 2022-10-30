package com.github.rehei.scala.dox.model.reference

import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.model.DoxInputReference
import com.github.rehei.scala.dox.util.HashUtils

case class DoxReferenceUtils(generatedPrefix: String) {

  protected val nextID = NextID("__" + generatedPrefix)

  def transform(input: Option[DoxReferenceBase]) = {

    val name = input.map(_.name).getOrElse(nextID.nextID())

    DoxInputReference(name)
  }

}