package com.github.rehei.scala.dox.util

import java.awt.Desktop
import java.io.File

trait ViewSupport {

  /*
   * Use as mix in for the main class so that 
   * the java.awt.headless property can be set as 
   * soon as possible!
   */

  System.setProperty("java.awt.headless", "false")

  object ViewEngine {

    def show(file: File) = {

      println("Viewing " + file.getCanonicalPath())

      java.awt.Desktop.getDesktop().open(file)
    }

  }

}