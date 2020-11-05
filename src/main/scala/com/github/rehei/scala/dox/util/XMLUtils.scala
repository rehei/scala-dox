package com.github.rehei.scala.dox.util

import java.io.StringWriter
import scala.xml.Node
import scala.xml.NodeSeq

object XMLUtils {

  def asString(node: NodeSeq) = {
    xml.Xhtml.toXhtml(node)
  }

  def parse(in: String) = {
    scala.xml.XML.loadString(in)
  }

}