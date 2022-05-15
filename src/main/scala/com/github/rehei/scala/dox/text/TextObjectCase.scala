package com.github.rehei.scala.dox.text

object TextObjectCase {
  
  val LOWERCASE = TextObjectCase(0)
  val UPPERCASE = TextObjectCase(1)
  val VARIANT = TextObjectCase(2)
  
}

case class TextObjectCase(id: Int)