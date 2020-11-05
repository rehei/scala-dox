package com.github.rehei.scala.dox.model

case class DoxDelegate(callback: () => Unit) extends AbstractDoxObject
