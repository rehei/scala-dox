package com.github.rehei.scala.dox.control

import com.github.rehei.scala.dox.model.DoxReference

case class DoxReferenceFactory(val prefix: String) {
  
  protected var current = 0
  
  def next() = {
    current = current + 1 
    DoxReference(prefix + current)
  }
  
}