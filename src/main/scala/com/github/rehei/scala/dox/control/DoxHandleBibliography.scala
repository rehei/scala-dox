package com.github.rehei.scala.dox.control

import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.StandardCharsets
import scala.collection.mutable.HashSet
import scala.collection.mutable.Map
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey

class DoxHandleBibliography(cache: DoxCacheBibliography) {

  protected val keys = HashSet[DoxBibKey]()

  protected val inverseKeyLookup = Map[String, String]()

  def append(key: DoxBibKey) {

    inverseKeyLookup.get(key.lookup().lookupKey()).map {
      referenceKey =>
        {
          if (key.name() != referenceKey) {
            throw new RuntimeException("LookupKeys should be referenced uniquely")
          }
        }
    } getOrElse {
      inverseKeyLookup.put(key.lookup().lookupKey(), key.name())
    }

    keys.add(key)
  }

  def writeTo(file: File) = {
    for (referenceKey <- keys) {
      write(file, cache.getOrUpdate(referenceKey))
    }
  }

  protected def write(file: File, content: String) = {
    FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, true)
  }

}