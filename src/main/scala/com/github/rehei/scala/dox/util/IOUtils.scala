package com.github.rehei.scala.dox.util

import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.nio.file.Path
import java.util.regex.Pattern
import scala.collection.mutable.ArrayBuffer

object IOUtils {

  def writeString(path: Path, content: String) = {
    Files.createDirectories(path.getParent())
    Files.write(path, content.getBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
  }

  def readString(path: Path) = {
    new String(Files.readAllBytes(path.resolve("cache.bib")))
  }

}