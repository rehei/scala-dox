package com.github.rehei.scala.dox.control

import java.nio.charset.StandardCharsets
import java.nio.file.Path

import org.apache.commons.io.FileUtils
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey

class DoxCacheBibliography(target: Path) {

  protected val map = scala.collection.mutable.Map[DoxBibKey, String]()

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
    val cacheFile = file(key)
    if (cacheFile.exists()) {
      Some(FileUtils.readFileToString(cacheFile, StandardCharsets.UTF_8))
    } else {
      None
    }
  }

  protected def lookupDelegate(key: DoxBibKey) = {
    key.lookup().resolve()
  }

  protected def updateCache(key: DoxBibKey, content: String) {
    map.put(key, content)
    FileUtils.writeStringToFile(file(key), content, StandardCharsets.UTF_8, true)
  }

  protected def file(key: DoxBibKey) = {
    val filename = friendlyFilename(key.name())
    target.resolve(filename + ".bib").toFile()
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