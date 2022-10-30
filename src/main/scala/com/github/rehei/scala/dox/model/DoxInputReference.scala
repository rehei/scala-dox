package com.github.rehei.scala.dox.model

import com.github.rehei.scala.dox.util.HashUtils

case class DoxInputReference(val name: String) {

  val filename = {
    hashID
  }

  val caption = {
    name + " | " + filename
  }

  protected def hashID = {
    HashUtils.hash(name)
  }

}