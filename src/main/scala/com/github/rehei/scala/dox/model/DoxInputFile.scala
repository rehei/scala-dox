package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.model.reference.DoxReferenceBase
import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.util.NextID
import com.github.rehei.scala.dox.util.HashUtils

case class DoxInputFile(val content: String, name: String) {

  def caption = {
    name + " | " + hashID
  }

  def filename = {
    hashID
  }

  protected def hashID = {
    HashUtils.hash(name)
  }

}