package com.github.rehei.scala.dox.control

import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.StandardCharsets
import scala.collection.mutable.HashSet
import scala.collection.mutable.Map
import com.github.rehei.scala.dox.model.bibliography.DoxBibKey
import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotUniqueException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

case class DoxHandleBibliography(cache: DoxCacheBibliography, map: DoxBibKeyCountMap) {

  protected val keys = HashSet[DoxBibKey]()
  protected val inverseKeyLookup = Map[String, String]()

  def append(key: DoxBibKey) {
    
    map.increase(key)
    

    inverseKeyLookup.get(key.lookup().lookupKey()).map {
      referenceKey =>
        {
          if (key.name() != referenceKey) {
            throw new DoxBibKeyNotUniqueException("LookupKeys should be referenced uniquely")
          }
        }
    } getOrElse {
      inverseKeyLookup.put(key.lookup().lookupKey(), key.name())
    }

    keys.add(key)
  }

  def writeTo(path: Path) = {
    for (referenceKey <- keys) {
      write(path, cache.getOrUpdate(referenceKey))
    }
  }

  protected def write(path: Path, content: String) = {
    Files.write(path, content.getBytes, StandardOpenOption.CREATE)
  }

}