package com.github.rehei.scala.dox.util

import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.nio.file.Path
import java.util.regex.Pattern
import scala.collection.mutable.ArrayBuffer
import java.nio.charset.StandardCharsets

object IOUtils {

  def writeString(path: Path, content: String) = {
    Files.createDirectories(path.getParent())

    val writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)

    try {
      writer.write(content)
    } finally {
      writer.close()
    }
  }

  def readString(path: Path) = {
    new String(Files.readAllBytes(path))
  }

}