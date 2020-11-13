package com.github.rehei.scala.dox.control.tex

import scala.collection.mutable.Stack
import java.io.File
import java.nio.charset.StandardCharsets
import com.github.rehei.scala.dox.util.IOUtils
import java.nio.file.Path

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

  def writeTo(path: Path) = {
    IOUtils.writeString(path, build())
  }

}