package com.github.rehei.scala.dox.model.bibliography

import java.nio.file.Path

import scala.collection.mutable.HashSet
import scala.collection.mutable.Map

import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotUniqueException
import com.github.rehei.scala.dox.util.IOUtils

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
    IOUtils.writeString(path, content)
  }

}