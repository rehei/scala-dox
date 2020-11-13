package com.github.rehei.scala.dox.util

import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.nio.file.Path

object IOUtils {

  def writeString(path: Path, content: String) = {
    Files.write(path, content.getBytes, StandardOpenOption.CREATE)
  }
  
  def readString(path: Path) = {
    new String(Files.readAllBytes(path.resolve("cache.bib")))
  }

}