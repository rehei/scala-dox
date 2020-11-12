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
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCache
import com.github.rehei.scala.dox.model.bibliography.DoxBibKeyCountMap

case class DoxBibKeyRendering(cache: DoxBibKeyCache, map: DoxBibKeyCountMap) {

  protected val keys = HashSet[DoxBibKey]()
  protected val inverseKeyLookup = Map[String, String]()

  def append(key: DoxBibKey) {
    
    map.increase(key)
    
    val result = cache.getOrUpdate(key)
    val content = result.normalize()

    inverseKeyLookup.get(content).map {
      referenceKey =>
        {
          if (key.name != referenceKey) {
            throw new DoxBibKeyNotUniqueException("LookupKeys should be referenced uniquely")
          }
        }
    } getOrElse {
      inverseKeyLookup.put(content, key.name)
    }

    keys.add(key)
  }

  def writeTo(path: Path) = {
    for (referenceKey <- keys) {
      write(path, cache.getOrUpdate(referenceKey).get())
    }
  }

  protected def write(path: Path, content: String) = {
    Files.write(path, content.getBytes, StandardOpenOption.CREATE)
  }

}