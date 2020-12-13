package com.github.rehei.scala.dox.control.tex

import scala.collection.mutable
import java.io.File
import java.nio.charset.StandardCharsets
import com.github.rehei.scala.dox.util.IOUtils
import java.nio.file.Path
import java.io.Writer

case class TexAST() {

  protected val stack = mutable.Stack[AbstractTexObject]()

  def append(in: AbstractTexObject) = {
    stack.push(in)
  }

  def reverse() = {
    stack.pop()
  }

  def build() = {
    stack.reverseIterator.map(_.generate()).mkString
  }

  def write(writer: Writer) = {
    writer.write(build())
  }

}