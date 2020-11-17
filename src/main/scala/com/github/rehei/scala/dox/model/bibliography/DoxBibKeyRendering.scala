package com.github.rehei.scala.dox.model.bibliography

import java.nio.file.Path

import scala.collection.mutable.HashSet
import scala.collection.mutable.Map

import com.github.rehei.scala.dox.model.ex.DoxBibKeyNotUniqueException
import com.github.rehei.scala.dox.util.IOUtils
import java.io.Writer
import org.apache.commons.lang3.StringUtils
import com.github.rehei.scala.dox.model.ex.DoxBibKeyValueBlankException

case class DoxBibKeyRendering(cache: DoxBibKeyCache, map: DoxBibKeyCountMap) {

  protected val keys = HashSet[DoxBibKey]()
  protected val inverseKeyLookup = Map[String, String]()

  def append(key: DoxBibKey) {

    if (StringUtils.isBlank(key.name)) {
      throw new DoxBibKeyValueBlankException("Name of key should not be blank.")
    }

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

  def writeTo(writer: Writer) = {
    for (referenceKey <- keys) {
      write(writer, cache.getOrUpdate(referenceKey).get())
    }
  }

  protected def write(writer: Writer, content: String) = {
    writer.append(content)
  }

}