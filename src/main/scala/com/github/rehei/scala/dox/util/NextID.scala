package com.github.rehei.scala.dox.util

case class NextID(val prefix: String) {
  
  protected var current = 0
  
  def next() = {
    current = current + 1 
    prefix + current
  }
  
}