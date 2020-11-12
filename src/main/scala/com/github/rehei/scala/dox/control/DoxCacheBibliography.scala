package com.github.rehei.scala.dox.control

import java.nio.charset.StandardCharsets
import java.nio.file.Path

import org.apache.commons.io.FileUtils
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import java.nio.file.Files
import scala.collection.JavaConversions._
import java.nio.file.StandardOpenOption

case class DoxCacheBibliography(target: Path) {

  if (!Files.exists(target)) {
    Files.createDirectories(target)
  }

  protected val map = scala.collection.mutable.Map[DoxBibKey, String]()

  def warmup(sequence: Seq[DoxBibKey]) = {
    for (key <- sequence) {
      getOrUpdate(key)
    }
  }

  def getOrUpdate(key: DoxBibKey) = {
    lookupMemoryCache(key).getOrElse {
      lookupPersistentCache(key).getOrElse {
        val content = lookupDelegate(key)
        updateCache(key, content)
        content
      }
    }
  }

  protected def lookupMemoryCache(key: DoxBibKey) = {
    map.get(key)
  }

  protected def lookupPersistentCache(key: DoxBibKey) = {
    val cacheFile = path(key)
    if (Files.exists(cacheFile)) {
      val tmp = new String(Files.readAllBytes(cacheFile))
      map.put(key, tmp)
      Some(tmp)
    } else {
      None
    }
  }

  protected def lookupDelegate(key: DoxBibKey) = {
    key.lookup().resolve()
  }

  protected def updateCache(key: DoxBibKey, content: String) {
    map.put(key, content)
    Files.write(path(key), content.getBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
  }

  protected def path(key: DoxBibKey) = {
    val filename = friendlyFilename(key.name())
    target.resolve(filename + ".bib")
  }

  protected def friendlyFilename(input: String) = {
    if (input == null || input == "") {
      input
    } else {
      input.toLowerCase()
        .replace("&", "-")
        .replace(" ", "-")
        .replace("(", "")
        .replace("\n", "---")
        .replace(")", "")
        .replace("[", "")
        .replace("]", "")
        .replace("\"", "'")
        .replace("ö", "oe")
        .replace("ü", "ue")
        .replace("ä", "ae")
        .replaceAll("[,?!.:;\\/+]", "-")
        .replaceAll("-+", "-")
        .replaceAll("-$", "")
        .replaceAll("^-", "")
    }
  }

}