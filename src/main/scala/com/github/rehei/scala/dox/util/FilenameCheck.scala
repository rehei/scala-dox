package com.github.rehei.scala.dox.util

import scala.collection.mutable.HashMap

object FilenameCheck {
  protected val usage = HashMap[String, Boolean]()
  def addFilename(filename: String) = {
    assert(usage.get(filename).isEmpty)
    usage.put(filename, true)
  }

}