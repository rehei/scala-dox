package com.github.rehei.scala.dox.util

case class NextID(prefix: String) {

  protected var current = 0

  def nextID() = {
    current = current + 1
    prefix + current
  }

}