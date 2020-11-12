package com.github.rehei.scala.dox

import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyEnum
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import scala.collection.mutable.ArrayBuffer
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyScanner

object ExampleTraversal {

  import scala.reflect.runtime.universe._

  def main(args: Array[String]): Unit = {

    val scanner = DoxBibKeyScanner.create[ExampleReference.type]

    for (key <- scanner.list) {
      println(key)
    }

  }

}