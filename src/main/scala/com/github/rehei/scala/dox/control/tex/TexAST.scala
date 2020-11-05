package com.github.rehei.scala.dox.control.tex

import org.apache.commons.io.FileUtils
import scala.collection.mutable.Stack
import java.io.File
import java.nio.charset.StandardCharsets

case class TexAST() {

  protected val stack = Stack[AbstractTexObject]()

  def append(in: AbstractTexObject) = {
    stack.push(in)
  }

  def reverse() = {
    stack.pop()
  }

  def build() = {
    stack.reverseIterator.map(_.generate()).mkString("\n")
  }

  def writeTo(file: File) = {
    FileUtils.writeStringToFile(file, build(), StandardCharsets.UTF_8)
  }

}